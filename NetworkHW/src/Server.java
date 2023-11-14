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
            System.out.println("서버가 포트 " + port + "에서 대기 중입니다.");

            while (true) {
                // 클라이언트의 연결을 수락하고 클라이언트 핸들러를 시작합니다.
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress() + "로부터의 연결을 수락했습니다.");

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
                // 클라이언트로부터의 입력을 처리하고 결과를 클라이언트에게 다시 전송합니다.
                if ("end".equalsIgnoreCase(inputLine)) {
                    break;
                }

                try {
                    // 식 평가
                    double result = evaluateExpression(inputLine);
                    out.println("결과: " + result);
                } catch (IllegalArgumentException | ArithmeticException ex) {
                    out.println("오류: " + ex.getMessage());
                }
            }

            // 클라이언트와의 연결을 종료합니다.
            System.out.println(clientSocket.getInetAddress() + "에서의 연결을 종료했습니다.");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double evaluateExpression(String expression) {
        // 간단한 식 평가
        String[] tokens = expression.split(" ");

        if (tokens.length != 3) {
            throw new IllegalArgumentException("잘못된 입력 형식입니다. 사용법: 연산자 피연산자1 피연산자2");
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
                    throw new ArithmeticException("0으로 나눌 수 없습니다");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("잘못된 연산자: " + tokens[0]);
        }
    }
}
