package client.player;

public class RegisterInformation {
    private int register;
    private boolean filled;

    public RegisterInformation(int register, boolean filled) {
        this.register = register;
        this.filled = filled;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}
