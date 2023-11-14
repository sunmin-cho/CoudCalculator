import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String serverIP = "localhost"; // ������ IP �ּ� ����
        int port = 12345; // ������ ��Ʈ ��ȣ ����

        try (
            Socket socket = new Socket(serverIP, port);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("������ ����Ǿ����ϴ�.");

            while (true) {
                // ����� ������� ����ϴ�.
                System.out.print("����� �����Ͻðڽ��ϱ�? (��: yes / �ƴϿ�: no): ");
                String continueCalculation = userInput.readLine().toLowerCase();

                if (continueCalculation.equals("no")) {
                    out.println("end");
                    break;
                }

                // ������ �Է¹ް� ������ �����մϴ�.
                System.out.print("������ �Է��ϼ��� (��: add 5 3): ");
                String userMessage = userInput.readLine();
                out.println(userMessage);

                // �������� �޾ƿ� ����� ����մϴ�.
                String serverResponse = serverIn.readLine();
                System.out.println("�������� �޾� �� ���:");
                System.out.println(serverResponse);
            }

            System.out.println("������ �����մϴ�.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
