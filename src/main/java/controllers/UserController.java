package controllers;

import models.User;

public class UserController extends User {

    public UserController(String username, String password, String nickname, String email, String role,
            String recovery_pass_question, String recovery_pass_answer, int level, int experience, int hitPoints)

    {
        super(username, password, nickname, email, role, recovery_pass_question, recovery_pass_answer, level,
                experience,
                hitPoints);
    }

    // other methods will be here if needed!

}
