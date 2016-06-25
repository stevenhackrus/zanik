import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Enchanced cache downloader
 * Handles cache downloading & unzipping
 * @author Gabriel Hannason
 */
public class CacheDownloader {

	public static void init() {
		try {
			for(CACHE_DOWNLOAD_FILES cacheFile : CACHE_DOWNLOAD_FILES.values()) {
				boolean exists = new File(signlink.findcachedir() + cacheFile.identifier).exists();
				if(!exists) {
					int total = cacheFile.file.length;
					int current = 1;
					for(String file : cacheFile.file) {
						downloadFile(cacheFile, file, current, total);
						if(file.endsWith(".zip")) {
							unzip(new File(signlink.findcachedir() + file));
						}
						current ++;
					}
					new File(signlink.findcachedir() + cacheFile.identifier).createNewFile();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadFile(CACHE_DOWNLOAD_FILES cacheFile, String file, int current, int total) throws IOException {
		String fileURL = "http://ruse-ps.com/client/cache/" + file;
		String downloadingText = Client.optimizeText(cacheFile.toString().toLowerCase());
		URL url = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.addRequestProperty("User-Agent", "Mozilla/4.76");
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10,
							disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
						fileURL.length());
			}

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = signlink.findcachedir() + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[4096];
			long startTime = System.currentTimeMillis();
			int downloaded = 0;
			long numWritten = 0;
			int length = httpConn.getContentLength();
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
				numWritten += bytesRead;
				downloaded += bytesRead;
				int percentage = (int)(((double)numWritten / (double)length) * 100D);
				int downloadSpeed = (int) ((downloaded / 1024) / (1 + ((System.currentTimeMillis() - startTime) / 1000)));
				String s = total > 1 ? "("+current+"/"+total+")" : "";
				drawLoadingText(percentage, (new StringBuilder()).append("Downloading "+downloadingText+""+s+": "+percentage+"% ").append("@ "+downloadSpeed+"Kb/s").toString());
			}

			outputStream.close();
			inputStream.close();

		} else {
			System.out.println("Ruse-PS.Com replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}


	private static void drawLoadingText(int amount, String text) {
		Client.loadingPercentage = amount;
		Client.loadingText = text;
	}

	private static void unzip(final File file) {
		try {
			InputStream in =  new BufferedInputStream(new FileInputStream(file));
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry e;
			while((e=zin.getNextEntry()) != null) {
				if(e.isDirectory()) {
					(new File(signlink.findcachedir() + e.getName())).mkdir();
				} else {
					if (e.getName().equals(file.getName())) {
						unzipPartlyArchive(zin, file.getName());
						break;
					}
					unzipPartlyArchive(zin, signlink.findcachedir() + e.getName());
				}
			}
			zin.close();
			file.delete();
		} catch(Exception e) {}
	}

	/**
	 * Unzips a partly archive
	 * @param zin	The zip inputstream
	 * @param s		The location of the zip file
	 * @throws IOException	The method can throw an IOException.
	 */
	private static void unzipPartlyArchive(ZipInputStream zin, String s) throws Exception {
		FileOutputStream out = new FileOutputStream(s);
		drawLoadingText(100, "Unpacking data..");
		byte [] b = new byte[1024];
		int len = 0;

		while ((len = zin.read(b)) != -1) {
			out.write(b,0,len);
		}
		out.close();
	}

	enum CACHE_DOWNLOAD_FILES {

		IMAGES(new String[]{"sprites.idx", "sprites.dat", "icon.png"}, "cache1loaded"),
		CACHE(new String[]{"ruse.dat", "ruse.idx0", "ruse.idx1", "ruse.idx2", "ruse.idx3", "ruse.idx4", "ruse.idx5", "ruse.idx6"}, "cache2loaded"),
		DATA(new String[]{"data.zip"}, "cache3loaded"),
		;

		CACHE_DOWNLOAD_FILES(String[] file, String identifier) {
			this.file = file;
			this.identifier = identifier;
		}

		private String[] file;
		private String identifier;
	}
}
