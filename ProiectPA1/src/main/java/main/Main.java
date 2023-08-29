package main;

import algorithm.Route;
import algorithm.TestCycles;
import customExceptions.InvalidChoseCircleException;
import customExceptions.InvalidLengthException;
import dao.NodeDAO;
import dao.StreetDAO;
import dao.UserDAO;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Node;
import models.Street;
import javafx.scene.input.MouseEvent;
import utils.AlertMessage;
import utils.ImportData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main extends Application {

    int nrMap;
    List<List<Node>> foundCyclesMap1;
    List<List<Node>> foundCyclesMap2;
    List<List<Node>> foundCyclesMap3;
    boolean isMap1Calculated = false, isMap2Calculated = false, isMap3Calculated = false;


    public void drawPath(Pane root, List<Node> nodesPath) {

        Node previousNode = nodesPath.get(0);
        System.out.println(nodesPath);
        for (int index = 1; index < nodesPath.size(); index++) {

            Line lineStreet = new Line();
            lineStreet.setStrokeWidth(2);
            lineStreet.setStroke(Color.RED);
            lineStreet.setStartX(previousNode.getX());
            lineStreet.setStartY(previousNode.getY());
            lineStreet.setEndX(nodesPath.get(index).getX());
            lineStreet.setEndY(nodesPath.get(index).getY());

            root.getChildren().add(lineStreet);

            Circle circleNode = new Circle();

            circleNode.setCenterX(nodesPath.get(index).getX());
            circleNode.setCenterY(nodesPath.get(index).getY());
            circleNode.setFill(Color.RED);
            circleNode.setRadius(10);

            root.getChildren().add(circleNode);
            previousNode = nodesPath.get(index);

        }
        Line lineStreet = new Line();
        lineStreet.setStrokeWidth(2);
        lineStreet.setStroke(Color.RED);
        lineStreet.setStartX(previousNode.getX());
        lineStreet.setStartY(previousNode.getY());
        lineStreet.setEndX(nodesPath.get(0).getX());
        lineStreet.setEndY(nodesPath.get(0).getY());

        Circle circleNode = new Circle();

        circleNode.setCenterX(nodesPath.get(0).getX());
        circleNode.setCenterY(nodesPath.get(0).getY());
        circleNode.setFill(Color.RED);
        circleNode.setRadius(10);
        root.getChildren().add(circleNode);
        root.getChildren().add(lineStreet);

    }

    public Node checkNodeInGraph(int x, int y, int nrMap) throws SQLException, InvalidChoseCircleException {
        NodeDAO nodesDao = new NodeDAO();
        List<Node> nodes = nodesDao.generateNodes(nrMap);
        int nodeSize = 10;

        for (Node node : nodes) {
            if (x >= (node.getX() - nodeSize / 2) && x <= (node.getX() + nodeSize / 2) &&
                    y > (node.getY() - nodeSize / 2) && y < (node.getY() + nodeSize / 2)) {
                return node;
            }
        }
        throw new InvalidChoseCircleException();
    }

    public void displayButtons(Pane root, Stage primaryStage) {

        MenuButton btnChooseMap = new MenuButton("Choose map:");
        MenuItem menuItem1 = new MenuItem("Map 1");
        MenuItem menuItem2 = new MenuItem("Map 2");
        MenuItem menuItem3 = new MenuItem("Map 3");
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pane newRoot = new Pane();
                Scene mapScene = new Scene(newRoot, 100, 100);
                primaryStage.setScene(mapScene);
                primaryStage.show();
                nrMap = 1;
                try {

                    displayNodes(newRoot, 1);
                    displayStreets(newRoot, 1);
                    displayButtons(newRoot, primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Pane newRoot = new Pane();
                Scene mapScene = new Scene(newRoot, 1000, 1000);
                primaryStage.setScene(mapScene);
                primaryStage.show();
                nrMap = 2;
                try {
                    displayNodes(newRoot, 2);
                    displayStreets(newRoot, 2);
                    displayButtons(newRoot, primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        menuItem3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pane newRoot = new Pane();
                Scene mapScene = new Scene(newRoot, 1000, 1000);
                primaryStage.setScene(mapScene);
                primaryStage.show();
                nrMap = 3;
                try {
                    displayNodes(newRoot, 3);
                    displayStreets(newRoot, 3);
                    displayButtons(newRoot, primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Font fontText = new Font(12);
        btnChooseMap.getItems().addAll(menuItem1, menuItem2, menuItem3);
        btnChooseMap.setLayoutY(600);
        btnChooseMap.setLayoutX(50);
        btnChooseMap.setPrefHeight(50);
        btnChooseMap.setPrefWidth(170);
        btnChooseMap.setFont(fontText);
        root.getChildren().add(btnChooseMap);

        Button btnChooseNode = new Button("Choose the start point.");
        btnChooseNode.setLayoutY(600);
        btnChooseNode.setLayoutX(270);
        btnChooseNode.setPrefHeight(50);
        btnChooseNode.setPrefWidth(170);
        btnChooseNode.setFont(fontText);
        root.getChildren().add(btnChooseNode);

        Label descLength = new Label("Write approximate length in m:");
        descLength.setLayoutY(600);
        descLength.setLayoutX(490);
        root.getChildren().

                add(descLength);

        TextField lengthField = new TextField();
        lengthField.setLayoutY(625);
        lengthField.setLayoutX(490);
        lengthField.setPrefWidth(250);
        root.getChildren().

                add(lengthField);

        if (btnChooseNode.isDisable()) {
            System.out.println(lengthField.getText());
        }

        Button findRoute = new Button("Find route");
        findRoute.setLayoutY(600);
        findRoute.setLayoutX(790);
        findRoute.setPrefHeight(50);
        findRoute.setPrefWidth(170);
        findRoute.setFont(fontText);
        root.getChildren().

                add(findRoute);

        btnChooseNode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        Node choseCircle = null;

                        try {
                            choseCircle = checkNodeInGraph((int) mouseEvent.getX(), (int) mouseEvent.getY(), nrMap);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (InvalidChoseCircleException e) {
                            e.printStackTrace();
                        }
                        if (choseCircle != null) {
                            Circle circleNode = new Circle();
                            circleNode.setCenterX(choseCircle.getX());
                            circleNode.setCenterY(choseCircle.getY());
                            circleNode.setFill(Color.RED);
                            circleNode.setRadius(10);
                            root.getChildren().add(circleNode);
                            btnChooseNode.setDisable(true);

                            if (lengthField.getText() != null) {

                                AlertMessage.messageBox("Cautarea unei rute va dura cateva secunde...");

                                Node finalChoseCircle = choseCircle;
                                TestCycles findCycles = new TestCycles();
                                findRoute.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {

                                        if (nrMap == 1 && !isMap1Calculated) {
                                            try {
                                                foundCyclesMap1 = findCycles.getCycles(1);

                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                            isMap1Calculated = true;
                                        }
                                        if (nrMap == 2 && !isMap2Calculated) {
                                            try {
                                                foundCyclesMap2 = findCycles.getCycles(2);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                            isMap2Calculated = true;
                                        }
                                        if (nrMap == 3 && !isMap3Calculated) {
                                            TestCycles findCycles3 = new TestCycles();
                                            try {
                                                foundCyclesMap3 = findCycles3.getCycles(3);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                            isMap3Calculated = true;
                                        }
                                        Pane newRoot = new Pane();
                                        Scene mapScene = new Scene(newRoot, 1000, 1000);
                                        primaryStage.setScene(mapScene);
                                        primaryStage.show();
                                        try {
                                            displayNodes(newRoot, nrMap);
                                            displayStreets(newRoot, nrMap);
                                            displayButtons(newRoot, primaryStage);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        int choseLength = Integer.parseInt(lengthField.getText());
                                        if (choseLength <= 300 || choseLength >= 10000) {

                                            try {
                                                throw new InvalidLengthException(lengthField.getText());
                                            } catch (InvalidLengthException e) {
                                                e.printStackTrace();
                                                return;
                                            }

                                        }


                                        Route alg = new Route();
                                        try {
                                            List<List<Node>> foundCurrentCycles = null;
                                            if (nrMap == 1) {
                                                foundCurrentCycles = foundCyclesMap1;
                                            } else if (nrMap == 2) {
                                                foundCurrentCycles = foundCyclesMap2;
                                            } else if (nrMap == 3) {
                                                foundCurrentCycles = foundCyclesMap3;
                                            }


                                            List<Node> foundCycle = alg.getCyclesFromNode(finalChoseCircle.getId(), nrMap,
                                                    choseLength, foundCurrentCycles);
                                            for (Node node : foundCycle) {
                                                System.out.println(node);
                                            }
                                            if (!foundCycle.isEmpty()) {
                                                drawPath(newRoot, foundCycle);
                                            } else {
                                                AlertMessage.messageBox("Nu s a gasit o ruta cu lungimea specificata");

                                            }

                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }


                        }
                    }
                });
            }
        });


    }

    public void displayNodes(Pane sceneRoot, int nrMap) throws Exception {
        NodeDAO nodesDao = new NodeDAO();
        List<Node> nodes;
        try {
            nodes = nodesDao.generateNodes(nrMap);
            for (Node node : nodes) {
                Circle circleNode = new Circle();
                circleNode.setCenterX(node.getX());
                circleNode.setCenterY(node.getY());
                circleNode.setRadius(10);
                sceneRoot.getChildren().add(circleNode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayStreets(Pane sceneRoot, int nrMap) throws Exception {

        StreetDAO streetsDao = new StreetDAO();
        NodeDAO nodesDao = new NodeDAO();

        List<Node> nodes;
        nodes = nodesDao.generateNodes(nrMap);

        List<Street> streets;
        streets = streetsDao.generateStreets(nrMap);

        for (Street street : streets) {

            Line lineStreet = new Line();
            lineStreet.setStrokeWidth(2);
            lineStreet.setStroke(Color.GRAY);

            for (Node node : nodes) {

                if (node.getId() == street.getIdNodeStart()) {

                    lineStreet.setStartX(node.getX());
                    lineStreet.setStartY(node.getY());
                }
                if (node.getId() == street.getIdNodeEnd()) {

                    lineStreet.setEndX(node.getX());
                    lineStreet.setEndY(node.getY());
                }
            }

            sceneRoot.getChildren().add(lineStreet);

        }
    }

    public void start(Stage primaryStage) throws Exception {
        Pane startRoot = new Pane();
        UserDAO userDao = new UserDAO();

        Button btnRegister = new Button("Register");
        TextField usernameBox = new TextField();
        PasswordField passwordBox = new PasswordField();
        Label usernameType = new Label("Type your username");
        Label passwordType = new Label("Type your password");
        Label loginText = new Label("Already have an account?");
        Button btnLogin = new Button("Login");
        usernameType.setLayoutX(75);
        usernameType.setLayoutY(80);
        passwordType.setLayoutX(75);
        passwordType.setLayoutY(135);
        usernameBox.setLayoutX(75);
        usernameBox.setLayoutY(100);
        passwordBox.setLayoutX(75);
        passwordBox.setLayoutY(155);
        btnRegister.setLayoutX(120);
        btnRegister.setLayoutY(195);
        loginText.setLayoutX(80);
        loginText.setLayoutY(230);
        btnLogin.setLayoutX(125);
        btnLogin.setLayoutY(255);
        Label successStatus = new Label("Registration completed succesfully\nGo further by pressing the login button");
        Label failStatus = new Label("Registration has failed\nPlease try again using a different name");
        btnRegister.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent arg0) {
                System.out.println("Register");
                try {
                    if (userDao.register(usernameBox.getText(), passwordBox.getText()) == true) {
                        successStatus.setLayoutX(75);
                        successStatus.setLayoutY(30);
                        startRoot.getChildren().remove(failStatus);
                        startRoot.getChildren().add(successStatus);
                    } else {
                        failStatus.setLayoutX(75);
                        failStatus.setLayoutY(30);
                        startRoot.getChildren().remove(successStatus);
                        startRoot.getChildren().add(failStatus);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println(usernameBox.getText());
                System.out.println(passwordBox.getText());

            }
        });
        startRoot.getChildren().add(btnRegister);
        startRoot.getChildren().add(usernameBox);
        startRoot.getChildren().add(passwordBox);
        startRoot.getChildren().add(usernameType);
        startRoot.getChildren().add(passwordType);
        startRoot.getChildren().add(loginText);
        startRoot.getChildren().add(btnLogin);

        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                startRoot.getChildren().remove(btnRegister);
                startRoot.getChildren().remove(loginText);
                startRoot.getChildren().remove(btnLogin);
                startRoot.getChildren().remove(failStatus);
                startRoot.getChildren().remove(successStatus);

                Button newLoginBtn = new Button("Login");
                newLoginBtn.setLayoutX(120);
                newLoginBtn.setLayoutY(195);
                startRoot.getChildren().add(newLoginBtn);

                newLoginBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        int userCurrentId = 0;
                        try {
                            userCurrentId = userDao.login(usernameBox.getText(), passwordBox.getText());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (userCurrentId != 0) {
                            Pane mapRoot = new Pane();
                            Scene mapScene = new Scene(mapRoot, 1000, 1000);
                            primaryStage.setTitle("RouteSeeker");
                            primaryStage.setScene(mapScene);
                            primaryStage.show();
                            nrMap = 1;
                            try {
                                displayNodes(mapRoot, 1);
                                displayStreets(mapRoot, 1);
                                displayButtons(mapRoot, primaryStage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Label loginFailed = new Label("Login has failed.\nPlease review your credentials");
                            loginFailed.setLayoutX(75);
                            loginFailed.setLayoutY(30);
                            startRoot.getChildren().add(loginFailed);
                        }
                    }
                });
            }
        });

        Scene scene = new Scene(startRoot, 300, 400);
        primaryStage.setTitle("Authentication");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void populateTables() throws IOException, SQLException {

        NodeDAO nodeDao = new NodeDAO();
        StreetDAO streetDAO = new StreetDAO();
        ImportData infos = new ImportData();

        for (Node node : infos.nodesImport()) {
            nodeDao.createNode(node);
        }

        for (Street street : infos.streetsImport()) {
            streetDAO.createStreet(street);
        }

    }

    public static void main(String[] args) throws IOException, SQLException {
        // populateTables();
        launch(args);
    }
}