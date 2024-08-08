package controllers;

import Account.Account;
import Account.GetAllAccount;
import authentication.Auth;
import authentication.UserAuthenticate;
import exceptions.BadPassword;
import exceptions.NoUser;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.User;
import sql.*;

import transaction.*;
import util.ConnectionManager;

import javax.management.DescriptorKey;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class UserController {
    UserAuthenticate userAuthenticate = new UserAuthenticate();

    public UserController() throws IOException {
    }

    public void userController(Javalin app, Connection conn) throws IOException {


        //get all the users
        GetUser getUser = new GetUser(ConnectionManager.getConn());
        app.get("/users", ctx -> {
            List<User> records = getUser.getUser(conn);
            if (records != null) {
                ctx.json(records);
            } else {
                ctx.status(500).result("Error fetching user records");
            }
        });


        // Get a user by username
        app.get("/users/{username}", ctx -> {
            String username = ctx.pathParam("username");
            User user = getUser.getUserByUsername( username);
            if (user != null) {
                ctx.json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        });

        //Get Account by username
        GetAccount getAccount = new GetAccount(ConnectionManager.getConn());
        app.get("/account/{username}", ctx -> {
            String username = ctx.pathParam("username");
            Account account = getAccount.getAccountByUsername(username);
            if (account != null) {
                ctx.json(account);
            } else {
                ctx.status(404).result("Account not found");
            }
        });

        //get transanction by account id
        app.get("/transaction/{account_id}", ctx -> {
            GetTransaction getTransaction = new GetTransaction();
            getTransaction.getTransactions(ctx, conn);
        });



        // create user
        CreateUser createUser = new CreateUser();
        app.post("/users",
                ctx -> {
                    createUser.createUser(ctx, conn);
                }
        );
        // create account
        CreateAccount createAccount = new CreateAccount();
        app.post("/account",ctx ->{
            createAccount.createAccount(ctx,conn);
        });

        //Delete Account
        DeleteAccount deleteAccount = new DeleteAccount();
        app.delete("/account" , ctx->{
            deleteAccount.deleteAccount(ctx, conn);
        });

        // Update first name and last name of a user
        app.put("/user/update/{username}", ctx -> {
            UpdateUser.updateUser(ctx, conn);
        });

        //User Log in
        app.post("/login",this::login);

        //Deposit path

        app.put("/deposit", ctx -> {
            Deposit.deposit(ctx, conn);
        });

        //Withdraw
        app.put("/withdraw", ctx -> {
            Withdrawal.withdraw(ctx, conn);
        });

        //Transfer money from one account to another \
        TransferMoney transferMoney = new TransferMoney();
        app.put("/transferMoney", ctx ->{
            transferMoney.transferMoney(ctx,conn);
        });


    }
    // lOg in
    public void  login(Context ctx) throws SQLException, NoUser, BadPassword {
    Auth auth = ctx.bodyAsClass(Auth.class);


        String user = userAuthenticate.authenticateUser(auth.getUsername(), auth.getPassword());
        ctx.json(user);
        ctx.status(200);


    }






}





