package GUID.GUID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import tw.edu.ym.guid.client.GuidClient;
import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.PII.Builder;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.edu.ym.guid.client.hashcode.GuidHashcodeGenerator;
import java.io.FileReader;

public class App {
	static String GUID_SERVER_URL = "https://120.126.47.138:8443";
	static String GUID_SERVER_USERNAME = "nycutest001";
	static String GUID_SERVER_PASSWORD = "Ksx6O%$i";
	static String GUID_SERVER_PREFIX = "NYCU01";

	public static void main(String[] args) throws URISyntaxException, IOException {

		String csvFile = "112students.csv"; // 從命令行參數中獲取CSV文件的路徑和文件名稱
		String line = "";
		String csvSplitBy = ","; // CSV文件中的分隔符
		String outputCsvFile = "newStudents.csv"; // 輸出的CSV文件名稱

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputCsvFile))) {
			int index = 0;
			
			while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                data = Arrays.copyOf(data, data.length + 1);

                if (index != 0) {
                	String firstName = String.valueOf(data[1].charAt(0));
					String lastName = data[1].substring(1);
					Sex sex = Sex.MALE;
					Birthday birthday = new Birthday(2001, 1, 1);
					String nationalId = "A123456789";
					String piiString = enCoder(firstName, lastName, sex, birthday, nationalId);

                    data[data.length - 1] = piiString;
                } else {
                    data[data.length - 1] = "GUID";
                }

                // 將CSV數據顯示在控制台上
                for (String item : data) {
                    System.out.print(item + " ");
                    // 將數據寫入CSV文件
                    bw.write(item + ",");
                }
                bw.write("\n"); // 換行
                System.out.println(); // 換行
                index++;
            }           
			System.out.println("CSV文件已成功生成：" + outputCsvFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		String firstName = "楊";
//        String lastName = "大寶";
//        Sex sex = Sex.MALE;
//        Birthday birthday = new Birthday(2015, 1, 31);
//        String nationalId = "A123456789";        
//        
//
//        String piiString = enCoder(firstName, lastName, sex, birthday, nationalId);
//        System.out.println(piiString);
	}

	public static String enCoder(String firstName, String lastName, Sex sex, Birthday birthday, String nationalId)
			throws URISyntaxException, IOException {
		PII pii = new PII.Builder(new Name(firstName, lastName), sex, birthday, new TWNationalId(nationalId)).build();
		GuidClient gc = new GuidClient(new URI(GUID_SERVER_URL), GUID_SERVER_USERNAME, GUID_SERVER_PASSWORD,
				GUID_SERVER_PREFIX);
		return gc.create(pii);
	}

}
