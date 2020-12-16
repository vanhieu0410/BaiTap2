import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileFactoty {
	//Lớp xử lí file, trong trường hợp này là file .txt
	//Tạo Map thay vì ArrayList vì Map tồn tại 2 cặp (Key, Value).
	//Key để lưu dữ liệu kiểu String, Value để lưu số lần xuất hiện trong file.
	
	private Map<String,Integer> wordMap = new HashMap<String, Integer>();
	//Tạo List<Map> để dùng cho việc sắp xếp, vì các Entry trong HashMap không sắp xếp được.
	//Cũng có thể dùng TreeMap để sắp xếp. 
	
	private List<Map.Entry<String,Integer>> listWord = new ArrayList<Map.Entry<String,Integer>>();
	//List<Map> thứ hai này tạo ra để lưu các giá trị khi ta tìm kiếm từ khóa.
	
	private List<Map.Entry<String,Integer>> listSearch = new ArrayList<Map.Entry<String,Integer>>();
	
	private static boolean IS_SORTED =	false;

	public void readFile(String url) {
		
		//Phương thức này dùng để đọc file từ ổ cứng.
		try
		{
			//Các phương thức liên quan đến đọc ghi file phải đặt trong try/catch 
			//vì có nhiều lỗi phát sinh như: file không tồn tại,file bị lỗi, file không có quyền đọc,...
			
			FileInputStream fileInputStream = new FileInputStream(url);//Dùng lớp này để đọc file dưới dạng các byte thô.
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,"UTF-8");//Lớp này là cầu nối chuyển fileInputStream từ dạng byte sang dạng (Character), định dạng ở đây là UTF-8
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);//lớp này để đọc văn bản bằng cách đệm và đọc liền mạch các kí tự.
			
			String line = bufferReader.readLine();//Tạo biến line kiểu String để lưu dữ liệu 1 dòng đọc được.
			while(line != null) { //Vòng lặp được lặp lại đọc từng dòng đến khi không còn dòng nào thì dừng.
				String []parts = line.split("[^a-zA-Z0-9]+");//Phương thức split("[...]") 
			
				for(String w: parts) //for each chạy từng phần tử trong mảng string vừa được gán giá trị.
				{
					if(wordMap.containsKey(w)) { //Kiểm tra xem Key đã tồn tại trong HashMap chưa":
						wordMap.put(w, wordMap.get(w) + 1);//Nếu có thì put entry<String,Integer> vào Map. Key là giá trị của mảng String ở trên, Value sẽ tăng thêm 1 sau mỗi lần gặp lại Key cũ.
					} else {
						wordMap.put(w, 1); //Nếu Key chưa tồn tại thì put entry<String,Integer> vào như trên nhưng value là 1
					}
				}
				line = bufferReader.readLine(); //Đọc từng dòng
			}
			listWord = new ArrayList<Map.Entry<String,Integer>>(wordMap.entrySet());
			bufferReader.close();//Đóng bufferReader để giải phóng bộ nhớ 
			inputStreamReader.close();//Đóng inPutStreamReader để giải phóng bộ nhớ
			fileInputStream .close();//fileInputStream cũng vậy
		}
		catch(Exception ex)
		{
			ex.printStackTrace();//Nếu có lỗi do việc đọc sẽ được in ra ở đây
		}
	}
	
	public void sortWord() {
		Collections.sort(listWord,new Comparator<Map.Entry<String,Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) 
			{
			if(	o1.getValue() < o2.getValue())   //So sánh 2 phần tử o1 và o2 nếu o1 nhỏ hơn o2 thì đổi chỗ.
			{
				return 1;						
			} else if( o1.getValue() == o2.getValue()) 
			{
				return 0;	
			} else {
				return -1;
			}	
			}		
		});	
		IS_SORTED = true; // Set lại giá trị cho biến IS_SORTED.
	}

	public void search(String keyWord) {
		//Phương thức này sẽ so sánh keyWord với các từ có trong listWord.
		//Có thể listWord chưa được sắp xếp nên ta dùng biến IS_SORTED để kiểm tra nếu là false ta sẽ gọi phương thức sort để thực hiện đúng yêu cầu bài toán.
		
		//long startTime = System.currentTimeMillis();
		if(!IS_SORTED)
		{
			sortWord();
		}
		if(keyWord.trim().length() > 0 && listWord.size() > 0) {//Check điều kiện là người dùng đã nhập từ khóa và file đã được định nghĩa ra thành listWord.
			//Search
			//Clear arrSearch
			
			listSearch.clear();//Nếu người dùng gọi phương thức search() nhiều lần sẽ bị lỗi Duplicate, nên ta phải xóa tác vụ trước đó.
			for (int i = 0; i < listWord.size(); i++ ) {//Duyệt lần lượt toàn bộ listWord.
				if(listWord.get(i).getKey().trim().toLowerCase().contains(keyWord.trim().toLowerCase()))
				{										//Lấy ra phần tử thứ i của list<Map.entry>,lấy Key tại đó, trim() để cắt ra, đưa về chữ thường và so sánh
					listSearch.add(listWord.get(i));	//với keyWord cũng được trim() và đưa về chữ thường. Nếu thỏa mãn thì add vào listSearch.
				}
			}
			if(listSearch.size() != 0) {//Check từ seach được mà trong list nhỏ hơn 5 mà lớn hơn 0 thì in ra độ dài là số phần tử của list
			 if(listSearch.size() < 5) {
					for(int i = 0; i < listSearch.size(); i++) {
						System.out.println( listSearch.get(i).getKey() + "    Số lần xuất hiện:   " + listSearch.get(i).getValue());
					}
				} else {
					for(int i = 0; i< 5; i++) //Nếu list lớn hơn 5 phần tử thì in ra 5 phần tử đầu.
						{
							System.out.println( listSearch.get(i).getKey() + "    Số lần xuất hiện:   " + listSearch.get(i).getValue());
							//In ra 5 phần tử của listSearch và getKey của nó. 5 phần tử này cũng là 5 ptu xuất hiện nhiều nhất.
						}
				}
			} else {
				System.out.println("Khong tim thay ki tu nao, vui long nhap lai");
			}
		
			//System.out.println("Thời gian search là: " + (System.currentTimeMillis() - startTime));
		} else {
			System.out.println("Bạn chưa nhập kí tự cần tìm kiếm: ");//Thông báo nếu không nhập chuỗi.
		}
	}
	
	public void countWord() {
		int count= 0;//Khai báo biến đếm.
		System.out.println("\nBảng liệt kê các từ cái xuất hiện trong file: ");
		System.out.println("Các từ ----------- Số lần xuất hiện");	
		for(Map.Entry<String, Integer> entry : listWord) {//Duyệt từng phần tử etry trong listWord.
			System.out.println(entry.getKey() +  "-----------" + entry.getValue());	//In ra màn hình và tăng biến đếm.
			count+= entry.getValue();
		}
		System.out.println("\nTổng số từ trong file là: ");
		System.out.println("\n " + count);
	}
	
	public void writeFile(String url) {
		
		try {
			//Làm việc với đọc ghi file phải đặt trong try/catch.
			FileOutputStream fileOutPutStream = new FileOutputStream(url);//Lưu file xuống ổ dưới dạng Byte.
			OutputStreamWriter outPutStreamWriter = new OutputStreamWriter(fileOutPutStream,"UTF-8"); //Cầu nối chuyển từ định dạng Character, ở đây là UTF-8 sang dạng byte.
			BufferedWriter bufferedWriter = new BufferedWriter(outPutStreamWriter);//Cung cấp bộ đệm để việc ghi file hiệu quả hơn.
			for(Map.Entry<String, Integer> entry : listWord) //Duyệt lần lượt từng entry trong listWord.
			{
				String word = entry.getKey() + " " + entry.getValue(); //Lưu giá trị Key và Value vào biến word.
				bufferedWriter.write(word);	//Ghi từng dòng cho đến khi kết thúc.
				bufferedWriter .newLine();
			}
			bufferedWriter.close();       //Đóng bufferedWriter, outputStreamWriter, fileOutPutStream để giải phóng bộ nhớ.
			outPutStreamWriter.close();
			fileOutPutStream.close();
		}catch(Exception ex) {
			ex.printStackTrace();      //Nếu ghi file có lỗi sẽ được in ra màn hình.
		}
	}
	
}
