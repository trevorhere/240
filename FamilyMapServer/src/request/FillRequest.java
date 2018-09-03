package request;

public class FillRequest {
    public FillRequest(String username)
    {
        this.username = username;
    }
    public FillRequest(String username, int generations)
    {
        this.generations = generations;
        this.username = username;
    }

    public String username;
    public int generations;

}
