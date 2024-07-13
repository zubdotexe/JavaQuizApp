package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import java.io.*;

public class Main {

    public static JSONArray loadUsers() throws IOException, ParseException {
        String file_name = "src/main/resources/users.json";
        JSONParser parser = new JSONParser();
        JSONArray uArr = (JSONArray) parser.parse(new FileReader(file_name));

        return uArr;
    }

    public static String userValidation(String name, String password) throws IOException, ParseException {
        JSONArray uArr = loadUsers();

        for(int i = 0; i < uArr.size(); i++) {
            JSONObject uObj = (JSONObject) uArr.get(i);
            String userName = (String) uObj.get("username");
            String userPassword = (String) uObj.get("password");
            String userRole = (String) uObj.get("role");

            if(name.equalsIgnoreCase(userName) && password.equalsIgnoreCase(userPassword)) {
                return userRole;
            }
        }

        return null;
    }

    public static void admin() throws IOException, ParseException {
        System.out.println("Welcome admin! Please create new questions in the question bank.\n");
        Scanner sc = new Scanner(System.in);

        setQuestions();

        while(true) {
            String userOpinion = sc.next();

            if(userOpinion.equalsIgnoreCase("q")) {
                System.out.println("Qutting the program...");
                break;
            }
            else if(userOpinion.equalsIgnoreCase("s")) setQuestions();
            else System.out.println("Please, enter either s or q");
        }
    }

    public static void student() throws IOException, ParseException {
        System.out.println("Welcome to the quiz! We will throw you 10 questions. Each MCQ mark is 1 and no negative marking. Are you ready? Press 's' for start.");
        Scanner sc = new Scanner(System.in);

        while(true) {
            String userOpinion = sc.next();

            if(userOpinion.equalsIgnoreCase("q")) {
                System.out.println("Quitting the program...");
                break;
            }
            else if(userOpinion.equalsIgnoreCase("s")) readQuestions();
            else System.out.println("Please enter either s or q");
        }
    }

    public static JSONArray loadQuestions() throws IOException, ParseException {
        String file_name = "src/main/resources/questions.json";

        JSONParser parser = new JSONParser();

        return (JSONArray) parser.parse(new FileReader(file_name));
    }

    public static void saveQuestions(JSONArray qArr) throws IOException {
        String file_name = "src/main/resources/questions.json";
        FileWriter writer = new FileWriter(file_name);
        writer.write(qArr.toJSONString());
        writer.flush();
        writer.close();
    }

    public static boolean isCorrectValue(String answerkey) {
        for(int i = 1; i <= 4; i++) {
            if(answerkey.equalsIgnoreCase(String.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

    public static void setQuestions() throws IOException, ParseException {
        JSONObject qObj = new JSONObject();
        JSONArray qArr = loadQuestions();

        Scanner sc = new Scanner(System.in);

        System.out.println("Input your question");
        String q = sc.nextLine();
        qObj.put("question", q);

        for(int i = 1; i < 5; i++) {
            System.out.println("Input option " + i + ":");
            String a = sc.nextLine();
            qObj.put("option " + i, a);
        }

        System.out.println("What is the answer key?");

        String answerkey = sc.next();

        if(isCorrectValue(answerkey)) {
            qObj.put("answerkey", answerkey);
        }
        else {
            while(true) {
                System.out.println("Please enter a value in between 1 and 4");
                answerkey = sc.next();
                if(isCorrectValue(answerkey)) {
                    qObj.put("answerkey", answerkey);
                    break;
                }
            }
        }

        qArr.add(qObj);

        saveQuestions(qArr);
        System.out.println("Saved successfully! Do you want to add more questions? (press s for start and q for quit)");
    }

    public static void readQuestions() throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        JSONArray qArr = loadQuestions();

        Random rnd = new Random();
        int marks = 0;

        for(int i = 0; i < 10; i++) {
//            int randomObj = i;
            int randomObj = rnd.nextInt(qArr.size());
            JSONObject qObj = (JSONObject) qArr.get(randomObj);
            System.out.println("[Question " + (i + 1) + "] " + (String) qObj.get("question"));

            for(int j = 1; j <= 4; j++) {
                String option = "option " + j;
                System.out.println(String.valueOf(j) + ". " + qObj.get(option));
            }

            String answer = (String) qObj.get("answerkey");
            String input = "";

            System.out.println("Your input: (Type a number in between 1 and 4)");

            try {
                input = sc.next();

                if(isCorrectValue(input)) {
                    if(answer.equalsIgnoreCase(input)) {
                        marks += 1;
                    }
                }
                else {
                    while(true) {
                        System.out.println("Please enter a value in between 1 and 4");
                        input = sc.next();
                        if(isCorrectValue(input)){
                            if(answer.equalsIgnoreCase(input)) {
                                marks += 1;
                            }
                            break;
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Please enter a valid number");
            }
        }

        if(marks >= 8)
            System.out.println("Excellent! You have got " + marks + " out of 10");

        else if(marks >= 5)
            System.out.println("Good. You have got " + marks + " out of 10");

        else if(marks >= 2)
            System.out.println("Very poor! You have got " + marks + " out of 10");

        else if(marks >= 0)
            System.out.println("Very sorry you are failed. You have got " + marks + " out of 10");

        System.out.println("Would you like to start again? press s for start or q for quit\n");
    }

    public static void main(String[] args) throws IOException, ParseException {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your username");
        String name = sc.next();

        System.out.println("Enter your password");
        String password = sc.next();

        if(userValidation(name, password) == null) {
            System.out.println("The username or password is incorrect");
        }
        else if(userValidation(name, password).contains("admin")) {
            admin();
        }
        else if(userValidation(name, password).contains("student")) {
            student();
        }

        System.out.println("The program has been quit");
    }
}