import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String serverIP = "localhost"; // 서버의 IP 주소 지정
        int port = 12345; // 서버의 포트 번호 지정

        try (
            Socket socket = new Socket(serverIP, port);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("서버에 연결되었습니다.");

            while (true) {
                // 계산을 계속할지 물어봅니다.
                System.out.print("계산을 진행하시겠습니까? (예: yes / 아니오: no): ");
                String continueCalculation = userInput.readLine().toLowerCase();

                if (continueCalculation.equals("no")) {
                    out.println("end");
                    break;
                }

                // 계산식을 입력받고 서버에 전송합니다.
                System.out.print("계산식을 입력하세요 (예: add 5 3): ");
                String userMessage = userInput.readLine();
                out.println(userMessage);

                // 서버에서 받아온 결과를 출력합니다.
                String serverResponse = serverIn.readLine();
                System.out.println("서버에서 받아 온 결과:");
                System.out.println(serverResponse);
            }

            System.out.println("연결을 종료합니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
