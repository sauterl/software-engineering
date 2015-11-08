package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

/**
 * Collection of static methods for copying files and analyzing them
 * 
 * @author Silvan
 * 
 */
class RepoFileUtils {

	/**
	 * Utilizes renameTo for maximum efficiency
	 */
	static void move(Path source, Path target) throws IOException {
		Path newTarget = Paths.get(target.toString(), source.getFileName()
				.toString());
		source.toFile().renameTo(newTarget.toFile());
	}

	/**
	 * Example usage: If you want to copy /mydata/ to /target/mydata, then
	 * source would be /mydata/ and target /target/
	 * 
	 * @param originalSize
	 *            Size of the orginial File. Doesn't get changed while
	 *            traversing the filetree
	 * 
	 * @throws IOException
	 *             If an error happens while moving the file
	 */
	static void copyRecursively(Path source, Path target, ProgressListener pl,
			long alreadyProcessedBytes, long originalSize) throws IOException {
		Path combinedPath = Paths.get(target.toString(), source.getFileName()
				.toString());

		if (source.toFile().isFile()) {
			copy(source, combinedPath, pl, alreadyProcessedBytes, originalSize);
			return;
		}

		// copy a directory
		copy(source, combinedPath, pl, alreadyProcessedBytes, originalSize);
		// alreadyProcessedBytes += source.toFile().length();

		for (File subfile : source.toFile().listFiles()) {
			Path subfilePath = Paths.get(combinedPath.toString(),
					subfile.getName());
			if (subfile.isFile()) {
				copy(subfile.toPath(), subfilePath, pl, alreadyProcessedBytes,
						originalSize);
				alreadyProcessedBytes += subfile.length();
				continue;
			}
			copyRecursively(subfile.toPath(), combinedPath, pl,
					alreadyProcessedBytes, originalSize);
			alreadyProcessedBytes += RepoFileUtils.getFileSize(subfile);
		}
	}

	/**
	 * Copies source to target. If source is a directory, just calls mkdirs Else
	 * buffers with an array of 1024 Bytes and calls the progressListener
	 * 
	 * @param source
	 * @param target
	 */
	static void copy(Path source, Path target,
			ProgressListener progressListener, long alreadyProcessed,
			long totalSize) {
		if(target.toFile().exists()){
			throw new IllegalArgumentException("The file you want to copy to already exists");
		}
		if (source.toFile().isDirectory()) {
			target.toFile().mkdirs();
			// alreadyProcessed += target.toFile().length();
			// progressListener.progress(alreadyProcessed, totalSize);
			return;
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(source.toFile());
			outputStream = new FileOutputStream(target.toFile());

			byte[] buffer = new byte[1000000];
			int size = 0;
			while ((size = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, size);
				alreadyProcessed += size;
				System.err.println(source.toString() + " | "
						+ target.toString() + " | " + alreadyProcessed); // TODO
				progressListener.progress(alreadyProcessed, totalSize);
			}
		} catch (IOException ex) {
			// ex.printStackTrace();
			throw new IllegalArgumentException("Error while moving file");
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				// silently ignored
			}
		}
	}

	/**
	 * Traverses the directory to give total size. If the file is a simple file,
	 * returns length
	 * 
	 * @return file size in bytes
	 */
	static long getFileSize(File directory) {
		if (directory.isFile()) {
			return directory.length();
		}
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				length += file.length();
				continue;
			}
			// length+=directory.length();
			length += getFileSize(file);
		}
		return length;
	}

	/**
	 * If the file is a directory: Iterates recursively over the given dir and
	 * counts number of files and dirs inside the file; If the file is a file:
	 * Return 1
	 * 
	 * @param file
	 * @return 1 if it's a file, else no. of folders and files inside given
	 *         directory
	 */
	static int getFileCount(File file) {
		if (file.isFile()) {
			return 1;
		}
		int count = 1;
		File[] subfiles = file.listFiles();

		if (subfiles != null) {
			for (int i = 0; i < subfiles.length; i++) {
				File newfile = subfiles[i];
				if (newfile.isFile()) {
					count++;
					continue;
				}
				count += getFileCount(newfile);
			}
		}
		return count;
	}

	public static void deleteRecursively(Path source) {
		System.out.println("deleting |"+source.toString());
		if (!source.toFile().exists()) {
			throw new IllegalArgumentException(
					"The given file to delete does not exist");
		}
		if (source.toFile().isFile()) {
			source.toFile().delete();
			return;
		}
		File[] files = source.toFile().listFiles();
		if (null != files) {
			for (File file : files) {
				if (file.isDirectory()) {
					deleteRecursively(file.toPath());
				} else {
					file.delete();
				}
			}
		}
		source.toFile().delete();
	}
}
