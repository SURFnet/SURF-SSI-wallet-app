package nl.eduid.wallet.bridge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;
import android.content.res.AssetManager;
import android.content.Context;

// TODO: Use a more reasonable approach to path handling, rather than reinventing a couple
// of classes due to the complete implementation missing from some Android versions

public class IrmaConfigurationCopier {

    private Context context;
    private AssetManager assetManager;

    private Path sourceConfigurationPath;
    public Path destAssetsPath;

    private ArrayList<Path> configurationFilePaths;

    public IrmaConfigurationCopier(Context context) throws IOException {
        this.context = context;
        this.assetManager = context.getAssets();

        this.sourceConfigurationPath = Paths.get("irma_configuration");
        this.destAssetsPath = Paths.get(context.getFilesDir().getPath()).resolve("assets");

        this.ensureDirExists(Paths.get(context.getFilesDir().getPath()).resolve("tmp"));
        this.ensureConfigurationAssets();
    }

    private void ensureConfigurationAssets() throws IOException {
        this.ensureDirExists(this.destAssetsPath);
        this.ensureDirExists(this.destAssetsPath.resolve("irma_configuration"));

        String schemes[] = this.assetManager.list(this.sourceConfigurationPath.toString());

        for(String scheme : schemes) {
            // Skip files (eg. READMEs) to get scheme folders
            Path sourceSchemePath = sourceConfigurationPath.resolve(scheme);
            if(this.assetManager.list(sourceSchemePath.toString()).length == 0)
                continue;

            Path destSchemePath = this.destAssetsPath.resolve(sourceSchemePath);
            this.ensureDirExists(destSchemePath);

            // Decide if we should copy the scheme, by comparing the timestamp in the bundle assets
            // and the one in our assets folder. If the latter does not exist or its value is smaller
            // than the one from the bundle assets, copy the scheme out of the bundle to the assets dir
            Path destTimestampPath = destSchemePath.resolve("timestamp");

            if (destTimestampPath.toFile().exists()) {
                try {
                    Path sourceTimestampPath = sourceSchemePath.resolve("timestamp");
                    long sourceTimestamp = this.readTimestamp(this.assetManager.open(sourceTimestampPath.toString()));
                    long destTimestamp = this.readTimestamp(new FileInputStream(destTimestampPath.toFile()));

                    if (sourceTimestamp <= destTimestamp)
                        continue;

                // If there's an error while checking timestamps, always copy
                } catch(NumberFormatException e) {
                    Log.e("AssetsCopier", "Failed to parse timestamps for scheme " + scheme, e);
                }
            }

            System.out.printf("Copying %s from assets\n", sourceSchemePath);
            this.copyScheme(sourceSchemePath);
            System.out.printf("Done copying %s from assets\n", sourceSchemePath);
        }
    }

    private void ensureDirExists(Path path) throws IOException {
        File dir = path.toFile();
        if (!dir.exists() && !dir.mkdir())
            throw new IOException("Could not create dir " + path);
    }

    private long readTimestamp(InputStream stream) throws IOException {
        return Long.parseLong(new BufferedReader(new InputStreamReader(stream)).readLine());
    }

    // Because AssetManager.list is terribly slow, we'll parse the irma_configuration files
    // out of the JAR manifest manually. Strip the assets/ prefix out of the
    // manifest path and sort the list
    private void collectConfigurationFilePaths() throws IOException {

        if(this.configurationFilePaths != null)
            return;

        Manifest manifest = new JarFile(this.context.getApplicationInfo().sourceDir).getManifest();

        Set<String> manifestEntries = manifest.getEntries().keySet();

        this.configurationFilePaths = new ArrayList<Path>(manifestEntries.size());
        Path manifestPrefix = Paths.get("assets").resolve(this.sourceConfigurationPath);

        for(String manifestEntry : manifestEntries) {
            Path manifestPath = Paths.get(manifestEntry);
            if(!manifestPath.startsWith(manifestPrefix))
                continue;

            this.configurationFilePaths.add(
                manifestPath.subpath(1, -1)
            );
        }

        Collections.sort(this.configurationFilePaths);
    }

    private void copyScheme(Path sourceSchemePath) throws IOException {
        this.collectConfigurationFilePaths();

        // Select all the files in the manifest for this scheme
        for (Path configurationFilePath : this.configurationFilePaths) {
            if (configurationFilePath.startsWith(sourceSchemePath)) {
                // Create a folder structure for this file, then copy over
                this.destAssetsPath.resolve(configurationFilePath.subpath(0, -2)).toFile().mkdirs();
                this.copyFile(configurationFilePath);
            }
        }
    }

    private void copyFile(Path path) throws IOException {
        InputStream sourceStream = this.assetManager.open(path.toString());

        Path destPath = this.destAssetsPath.resolve(path);
        OutputStream destStream = new FileOutputStream(destPath.toFile());

        byte[] buffer = new byte[1024];
        int read;
        while ((read = sourceStream.read(buffer)) != -1)
            destStream.write(buffer, 0, read);

        sourceStream.close();
        destStream.flush();
        destStream.close();
    }
}
