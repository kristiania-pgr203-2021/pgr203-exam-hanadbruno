package no.kristiania;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static no.kristiania.QuestionDao.createDataSource;

public class HttpServer {
    private final ServerSocket serverSocket;
    private Path rootDirectory;
    private List<String> listOfQuestions = new ArrayList<>();
    private List<Question> questionsList = new ArrayList<>();
    private QuestionDao questionDao;

    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);

        new Thread(this::handleClients).start();
    }

    private void handleClients() {
        try {
            while (true) {
                handleClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient() throws IOException {
        Socket clientSocket = serverSocket.accept();

        HttpMessage httpMessage = new HttpMessage(clientSocket);
        String[] requestLine = httpMessage.lineStart.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String fileTarget;
        String query = null;
        if (questionPos != -1) {
            fileTarget = requestTarget.substring(0, questionPos);
            query = requestTarget.substring(questionPos+1);
        } else {
            fileTarget = requestTarget;
        }

        if (fileTarget.equals("/api/alternativeAnswers")) {
            String responseText = "";
            for(Question question : questionsList)
            {
                responseText += "<p>" + question.getQuestionTitle() + ", " + "(" + question.getQuestionText() +")</p>";
            }
            respondWithContent(clientSocket, responseText, "text/html; charset=utf-8\n\n");;

        } else if (fileTarget.equals("/api/questions")) {
            Map<String, String> parameters = parseQueryParameters(httpMessage.getMessageBody());

            Question questions = new Question();

            questions.setId(parameters.get(0));
            questions.setQuestionTitle(parameters.get("title"));
            questions.setQuestionText(parameters.get("text"));
            questions.setQuestionOptionLowLabel(parameters.get("low_label"));
            questions.setQuestionOptionHighLabel(parameters.get("high_label"));
            questionsList.add(questions);
            String addedQuestionText = "You have added a question";



            respondWithContent(clientSocket, "Your question is added!" + questions,    "text/html; charset=utf-8\n\n");
        } else if (fileTarget.equals("/api/questionOptions")) {
            String responseText = "";

            for (int i = 0, categorySize = listOfQuestions.size(); i < categorySize; i++) {
                responseText += "<option='" + (i+1) + "'>" + listOfQuestions.get(i) + "</option>";;
            }

            respondWithContent(clientSocket, responseText, "text/html;");
        } else {
            if (rootDirectory != null && Files.exists(rootDirectory.resolve(fileTarget.substring(1)))) {
                String responseText = Files.readString(rootDirectory.resolve(fileTarget.substring(1)));

                String contentType = "text/html";
                if (requestTarget.endsWith(".html")) {
                    contentType = "text/html";

                }
                respondWithContent(clientSocket, responseText, contentType);
                return;
            }

            String responseText = "File not found :  " + requestTarget;

            String response = "HTTP/1.1 404 Not found\r\n" +
                    "Content-Length: " + responseText.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    responseText;
            clientSocket.getOutputStream().write(response.getBytes());
        }
    }

    private Map<String, String> parseQueryParameters(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            int equalsPos = queryParameter.indexOf('=');
            String parameterName = queryParameter.substring(0, equalsPos);
            String parameterValue = queryParameter.substring(equalsPos+1);
            queryMap.put(parameterName, parameterValue);
        }
        return queryMap;
    }

    private void respondWithContent(Socket clientSocket, String responseText, String contentType) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + responseText.length() + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseText;
        clientSocket.getOutputStream().write(response.getBytes());
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(1963);
        httpServer.setQuestionDao(new QuestionDao(createDataSource()));
        httpServer.setRoot(Paths.get("."));

    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setRoot(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public void setListOfQuestions(List<String> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }

    public List<Question> getQuestionsList() {
        return questionsList;
    }


    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }
}
