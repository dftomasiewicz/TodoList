package com.dft.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoData {
    private static TodoData instance = new TodoData();
    private static String filename = "TodoListItems.txt"; //tworzenie pliku gdzie beda zapisywane dane todo

    private ObservableList<TodoItem> todoItems; //ObservableList - pomaga w data binding (powiazaniu z danymi)
//    poza tym pozwala na wiecej niz jedna zmiane w jednym czasie (wywolanie wiecej niz jednego zdarzenia/eventu)
    private DateTimeFormatter formatter; //manimpulacja formatem daty

    public static TodoData getInstance(){
        return instance;
    }

    private TodoData() { //prywatny konstruktor chroni przed utworzeniem nowej instancji tej klasy
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

//    usuwamy bo juz wiecej nie uzywamy tej metody
//        public void setTodoItems(List<TodoItem> todoItems) {
//        this.todoItems = todoItems;
//    }

    public void addTodoItem(TodoItem item){
        todoItems.add(item);
    }

    public void loadTodoItems() throws IOException {

        todoItems = FXCollections.observableArrayList(); //FXCollections zawiera metody statyczne 1:1 jak w klasie Collection
//        sa to takie same metody tylko zwracaja ObservableList i daja ograniczona liczbe powiadomien, przez co sa bardziej wydajne
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);
                TodoItem todoItem = new TodoItem(shortDescription, details, date);
                todoItems.add(todoItem);
            }

        } finally {
            if(br != null){
                br.close();
            }
        }
    }

    public void storeTodoItems() throws IOException {

        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<TodoItem> iter = todoItems.iterator();
            while (iter.hasNext()) {
                TodoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s", //%s produkuje string, jak sie napise duze S to konwertuje do uppercase'a
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadline().format(formatter)));
                bw.newLine();
            }

        } finally {
            if(bw != null) {
                bw.close();
            }
        }
    }

    public void deleteTodoItem(TodoItem item){
        todoItems.remove(item);
    }




}
