package aa.schedule;

//https://woowabros.github.io/tools/2017/07/10/java-enum-uses.html
public enum ScheduleResponseCode {
    ACK("ACK"), 
    NAK("NAK");

    private String value;

    ScheduleResponseCode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}