package vnu.uet.moonbe.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class YoutubeDownloader {

	public static void main(String[] args) {
		String[] videoUrls = {
				"https://www.youtube.com/watch?v=gQlMMD8auMs&list=OLAK5uy_nqFlTmDaFi5FSUCjjtTOfdk3G5-TExj_U",
				"https://www.youtube.com/watch?v=2GRP1rkE4O0&list=OLAK5uy_nj1uHGGbg8DnvMG7h58btD9lEOWR523iI&index=3",
				"https://www.youtube.com/watch?v=3SDHRPxVYNU&list=OLAK5uy_mdX_EHpnZ57Lke6ZcBmVxu_9lNA6MtI2c",
				"https://www.youtube.com/watch?v=1jTSZWRFEB8&list=OLAK5uy_mrebmodEHFO1PAdb3mwBIcY0FinZ_KitA",
		};

//		String videoUrl = "https://www.youtube.com/watch?v=Ws-QlpSltr8";
		String ytDlpPath = "D:\\Server\\yt-dlp.exe"; // Đường dẫn đầy đủ của yt-dlp
		String downloadDirectoryFile = "D:\\Repo\\file";
		String downloadDirectoryJson = "D:\\Repo\\json";

		for (String videoUrl : videoUrls) {
		// Thực hiện câu lệnh 1
		executeCommand(ytDlpPath, "-x", "--audio-format", "mp3", "--embed-metadata", "--add-metadata", "-o", downloadDirectoryFile + "\\%(title)s.%(ext)s", videoUrl);

		// Thực hiện câu lệnh 2
		executeCommand(ytDlpPath, "--skip-download", "--write-info-json", "-o", downloadDirectoryJson + "\\%(title)s.%(ext)s", videoUrl);
		}
	}

	private static void executeCommand(String... command) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();

			// Đọc output từ quá trình
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			}

			// Chờ quá trình kết thúc
			int exitCode = process.waitFor();
			System.out.println("Exit Code: " + exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}



