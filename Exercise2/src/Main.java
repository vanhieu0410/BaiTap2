import java.util.Scanner;

public class Main {
	private static String urlRead = "C:\\Users\\admin\\Downloads\\BaiTap.txt";
	private static String urlWrite = "C:\\Users\\admin\\Downloads\\BaiTap2.txt";
	private static String keyWord = "";

	public static void main(String[] args) {
		
		FileFactoty fileFactory = new FileFactoty();    //Khởi tạo lớp.
		Scanner sc = new Scanner(System.in);			//Dùng cho việc lấy giá trị nhập từ bàn phím.
		
		fileFactory.readFile(urlRead);
		fileFactory .sortWord();
		fileFactory.countWord();
		fileFactory.writeFile(urlWrite);
		
		System.out.println("Kí tự bạn muốn tìm kiếm là : \n");
		keyWord = sc.nextLine();						//Gán giá trị nhập từ bàn phím vào biến keyWord.
		fileFactory .search(keyWord);
		
		
		
		
		
		
		
}
}
