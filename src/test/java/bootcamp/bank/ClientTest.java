package bootcamp.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
    @Test
    public void testClient() {
        Client  client = new Client("Andreas", "Mustermann", "12233");
        assertEquals("Andreas", client.firstName());
        assertEquals("Mustermann", client.lastName());
        assertEquals("12233", client.customerId());
    }
}