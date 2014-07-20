package net.takanotsume.yo;

public class YoServiceTest {
    
    /* fill token and username when you execute tests. */
    private String token = "";
    private String username = "";
    
    // @Test
    public void sendYoAll_should_send_yo_all_subscribers()
            throws YoFailedException {
        YoService.sendYoAll(token);
        
    }
    
    // @Test
    public void sendYo_should_send_yo_individual_users()
            throws YoFailedException {
        YoService.sendYo(token, username);
    }
    
}
