package hq.demo.net.model;

import java.util.List;

public class WeatherList {
    private String success;

    private List<Result> result;

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return this.success;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Result> getResult() {
        return this.result;
    }
}
