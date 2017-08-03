package tk.samgrogan.al;

/**
 * Created by ghost on 7/5/2017.
 */

public class CommandModel {

    public String mUserCommand;
    public String mUser;

    public CommandModel(String mUserCommand, String mUser){
        this.mUserCommand = mUserCommand;
        this.mUser = mUser;
    }

    public CommandModel(){}

    public String getmUserCommand() {
        return mUserCommand;
    }

    public void setmUserCommand(String mUserCommand) {
        this.mUserCommand = mUserCommand;
    }
}
