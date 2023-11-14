import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("������ ��Ʈ " + port + "���� ��� ���Դϴ�.");

            while (true) {
                // Ŭ���̾�Ʈ�� ������ �����ϰ� Ŭ���̾�Ʈ �ڵ鷯�� �����մϴ�.
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress() + "�κ����� ������ �����߽��ϴ�.");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() throws NumberFormatException {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Ŭ���̾�Ʈ�κ����� �Է��� ó���ϰ� ����� Ŭ���̾�Ʈ���� �ٽ� �����մϴ�.
                if ("end".equalsIgnoreCase(inputLine)) {
                    break;
                }

                try {
                    // �� ��
                    double result = evaluateExpression(inputLine);
                    out.println("���: " + result);
                } catch (IllegalArgumentException | ArithmeticException ex) {
                    out.println("����: " + ex.getMessage());
                }
            }

            // Ŭ���̾�Ʈ���� ������ �����մϴ�.
            System.out.println(clientSocket.getInetAddress() + "������ ������ �����߽��ϴ�.");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double evaluateExpression(String expression) {
        // ������ �� ��
        String[] tokens = expression.split(" ");

        if (tokens.length != 3) {
            throw new IllegalArgumentException("�߸��� �Է� �����Դϴ�. ����: ������ �ǿ�����1 �ǿ�����2");
        }

        double operand1 = Double.parseDouble(tokens[1]);
        double operand2 = Double.parseDouble(tokens[2]);

        switch (tokens[0]) {
            case "add":
                return operand1 + operand2;
            case "sub":
                return operand1 - operand2;
            case "mul":
                return operand1 * operand2;
            case "div":
                if (operand2 == 0) {
                    throw new ArithmeticException("0���� ���� �� �����ϴ�");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("�߸��� ������: " + tokens[0]);
        }
    }
}
