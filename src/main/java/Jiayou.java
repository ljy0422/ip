import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.*;
import java.io.*;

public class Jiayou {
    private static final String LINE = "____________________________________________________________";
    private Path filePath = Paths.get("./data/tasks.txt");
    private ArrayList<Task> taskList = new ArrayList<Task>();

    private static enum CommandType {
        TODO, DEADLINE, EVENT, LIST, DELETE, MARK, UNMARK, BYE
    }

    public Jiayou() {
        loadTasksFromFile();
    }

    private void printList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < this.taskList.size(); i++) {
            Task task = this.taskList.get(i);
            System.out.println((i + 1) + "." + task.toString());
        }
    }

    private void loadTasksFromFile() {
        try {
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath);
                for (String line : lines) {
                    taskList.add(parseFromFile(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Task task : taskList) {
                writer.write(task.toStringForStore());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Task parseFromFile(String string) {
        String[] parts = string.split(" \\| ", 3);
        String command = parts[0];
        String isDone = parts[1];
        String content = parts[2];
        Task newTask = null;

        switch (command) {
            case "T":
                newTask = new ToDo(content);
                break;
            case "E":
                String event = content.split(" \\| ", 2)[0];
                String[] eventParts = content.split(" \\| ", 2)[1].split(" from | to ");
                newTask = new Event(event, eventParts[0], eventParts[1]);
                break;
            case "D":
                String[] deadlineParts = content.split(" \\| by");
                newTask = new Deadline(deadlineParts[0], deadlineParts[1]);
                break;
        }

        newTask.setStatus((isDone == "1"));
        return newTask;
    }

    private void parseFromInput(String input) {
        try {
            String[] parts = input.split(" ", 2);
            String commandString = parts[0];
            CommandType command = CommandType.valueOf(commandString.toUpperCase());
            String content = parts.length > 1 ? parts[1] : "";

            switch (command) {
            case LIST:
                printList();
                break;
            case MARK:
                if (content.isEmpty()) {
                    throw new ParseException("OOPS!!! I don't know which task to mark. Please add the index after the keyword mark!");
                } else {
                    markTask(content);
                    break;
                }
            case UNMARK:
                if (content.isEmpty()) {
                    throw new ParseException("OOPS!!! I don't know which task to unmark. Please add the index after the keyword unmark!");
                } else {
                    unmarkTask(content);
                    break;
                }
            case DELETE:
                if (content.isEmpty()) {
                    throw new ParseException("OOPS!!! I don't know which task to delete. Please add the index after the keyword delete!");
                } else {
                    deleteTask(content);
                    break;
                }
            case TODO:
                if (content.isEmpty()) {
                    throw new ParseException("OOPS!!! The description of a todo cannot be empty. Please add a description after the keyword todo!");
                } else {
                    ToDo newToDo = new ToDo(content);
                    taskList.add(newToDo);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newToDo);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    break;
                }
            case DEADLINE:
                String[] deadlineParts = content.split(" /by ");
                Deadline newDeadline = new Deadline(deadlineParts[0], deadlineParts[1]);
                taskList.add(newDeadline);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newDeadline);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                break;
            case EVENT:
                String[] eventParts = content.split(" /from | /to ");
                Event newEvent = new Event(eventParts[0], eventParts[1], eventParts[2]);
                taskList.add(newEvent);
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newEvent);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                break;
            }
            saveTasksToFile();
        } catch (IllegalArgumentException e) {
            System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteTask(String content) {
        try {
            int taskId = Integer.parseInt(content) - 1;
            if (taskId < 0 || taskId >= taskList.size()) {
                int max = taskList.size();
                throw new ParseException("OOPS!!! The task you wanna delete doesn't exist. Please input a valid number!");
            }

            Task removedTask = taskList.remove(taskId);
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removedTask);
            System.out.println("Now you have " + taskList.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            System.out.println("OOPS!!! Please enter a valid task number!");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }


    private void markTask(String content) {
        int taskId = Integer.parseInt(content);
        Task task = this.taskList.get(taskId - 1);
        task.setStatus(true);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    private void unmarkTask(String content) {
        int taskId = Integer.parseInt(content);
        Task task = taskList.get(taskId - 1);
        task.setStatus(false);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    public static void main(String[] args) {
        Jiayou jiayou = new Jiayou();
        Scanner sc = new Scanner(System.in);
        System.out.println(Jiayou.LINE);
        System.out.println("Hello! I'm Jiayou.");
        System.out.println("What can I do for you?");
        System.out.println(Jiayou.LINE);

        while (true) {
            String input = sc.nextLine();
            System.out.println(Jiayou.LINE);

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(Jiayou.LINE);
                break;
            } else {
                jiayou.parseFromInput(input);
            }
            System.out.println(Jiayou.LINE);
        }
        sc.close();
    }
}
