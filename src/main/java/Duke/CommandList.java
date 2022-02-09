package duke;

import task.Task;
import task.ToDo;
import task.Deadline;
import task.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandList {

    /**
     * Executes user's input.
     *
     * @param input command to execute
     * @param list Arraylist of Tasks to manipulate
     * @param storage Storage object to load/store list of Tasks
     * @return String containing the reply from Duke to the user
     */
    public String execute(String input, ArrayList<Task> list, Storage storage) {
        String[] inputs = input.split(" ");
        String firstWord = inputs[0];

        String output = "";

        if (input.equals("bye")) { // check for exit condition
            output = "Bye. Hope to see you again soon!";
        } else if (input.equals("list")) { // list all tasks
            output = "Here are the tasks in your list:\n";
            for (int i = 0; i < list.size(); i++) {
                output = output + ((i + 1) + "." + list.get(i)) + "\n";
            }
        } else if (firstWord.equals("mark") || firstWord.equals("unmark")) { // mark/unmark task
            String secondWord = inputs[1];
            int index = Integer.parseInt(secondWord);

            if (firstWord.equals("mark")) {
                Task targetTask = list.get((index - 1));
                output = targetTask.mark();
            } else {
                Task targetTask = list.get((index - 1));
                output = targetTask.unmark();
            }
            storage.store(list);
        } else if (firstWord.equals("delete")) { // delete task
            String secondWord = inputs[1];
            int index = Integer.parseInt(secondWord);

            Task targetTask = list.get((index - 1));
            list = targetTask.delete(list, (index - 1));
            output = "Noted. I've removed this task: \n" + targetTask + "\n";
            output = output + "Now you have " + list.size() + " tasks in your list.";
            storage.store(list);
        } else if (firstWord.equals("todo")) { // adds a todo task
            if (inputs.length == 1) {
                output = "☹ OOPS!!! The description of a todo cannot be empty.";
            } else {
                ToDo toDo = new ToDo(input.substring((5)));
                list.add(toDo);
                output = "Got it. I've added this task:\n"
                        + toDo + "\nNow you have " + list.size() + " tasks in your list.";
                storage.store(list);
            }
        } else if (firstWord.equals("deadline") || firstWord.equals("event")) {
            int start = input.indexOf("/");
            if (start == -1) { // if / char cannot be found
                output = "Please enter a valid date!";
            }
            String date = input.substring(start + 4);
            if (firstWord.equals("deadline")) { // adds a deadline task
                if (inputs.length == 1) {
                    output = "☹ OOPS!!! The description of a deadline cannot be empty.";
                }
                try {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
                    Date formattedDate = dateFormatter.parse(date);
                    String dateToString = new SimpleDateFormat("MMM-dd-yyyy HHmm").format(formattedDate);

                    Deadline deadline = new Deadline(input.substring(9, start - 1), dateToString);
                    list.add(deadline);
                    output = "Got it. I've added this task:\n"
                            + deadline + "\nNow you have " + list.size() + " tasks in your list.";
                    storage.store(list);
                } catch (ParseException e) {
                    output = "Please enter a valid date!";
                }
            } else { // adds an event task
                if (inputs.length == 1) {
                    output = "☹ OOPS!!! The description of an event cannot be empty.";
                }
                try {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
                    Date formattedDate = dateFormatter.parse(date);
                    String dateToString = new SimpleDateFormat("MMM-dd-yyyy HHmm").format(formattedDate);

                    Event event = new Event(input.substring(6, start - 1), dateToString);
                    list.add(event);
                    output = "Got it. I've added this task:\n"
                            + event + "\nNow you have " + list.size() + " tasks in your list.";
                    storage.store(list);
                } catch (ParseException e) {
                    output = "Please enter a valid date!";
                }
            }
        } else if (firstWord.equals("find")) { // find tasks by a keyword
            String secondWord = inputs[1];
            List<Task> matches = new ArrayList<>();

            for(Task task: list) {
                if (task.toString().contains(secondWord)) {
                    matches.add(task);
                }
            }

            if (matches.isEmpty()) {
                output = "No matching tasks found.";
            } else {
                output = "Here are the matching tasks in your list:\n";
                for (int i = 0; i < matches.size(); i++) {
                    output = output + ((i + 1) + "." + matches.get(i)) + "\n";
                }
            }
        } else { // print error message
            output = "☹ OOPS!!! I'm sorry, but I don't know what that means :-(";
        }
        return output;
    }
}
