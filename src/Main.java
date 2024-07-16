import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Scanner;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {

        int valor=0;
        double monto = 0;
        String Moneda = "";
        Gson gson = new Gson();
        // MENU DE OPCIONES
        while (true) {
            System.out.println("""
                    
                    **************************
                    Selecciona una opción dentro del menú:
                    1 - USD a ARS
                    2 - USD a BRL
                    3 - USD a COP
                    4 - USD a MXN
                    5 - MXN a USD
                    6 - BRL a USD
                    7 - ARS a USD
                    8 - COP a USD
                    9 - Salir
                    **************************
                    """);
            Scanner Opcion = new Scanner(System.in);
            valor = Opcion.nextInt();

            switch (valor) {
                case 1, 2, 3, 4 -> Moneda = "USD";
                case 5 -> Moneda = "MXN";
                case 6 -> Moneda = "BRL";
                case 7 -> Moneda = "ARS";
                case 8 -> Moneda = "COP";
                case 9 -> {

                    System.out.println(" Gracias por usar el conversor ");
                    return; // Salir del programa
                }
                default -> {
                    System.out.println("Selecciona una opción correcta");
                    continue;
                }
            }

            HttpClient client = HttpClient.newHttpClient();
            // API Key de Exchangerate
            URI Llave = URI.create("https://v6.exchangerate-api.com/v6/74a6b77919ce9298db5ca223/latest/" + Moneda);
            HttpRequest request = HttpRequest.newBuilder().uri(Llave).build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String json = response.body();

                JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
                if (jsonObject != null && jsonObject.has("conversion_rates")) {
                    JsonObject MonedaObject = jsonObject.getAsJsonObject("conversion_rates");

                    System.out.println("Ingresa el monto que quiere convertir: ");
                    Scanner Monto = new Scanner(System.in);
                    monto = Monto.nextDouble();

                    if (MonedaObject != null) {
                        double conversion = 0;
                        switch (valor) {
                            case 1:
                                conversion = monto * MonedaObject.get("ARS").getAsDouble();
                            case 2:
                                conversion = monto * MonedaObject.get("BRL").getAsDouble();
                            case 3:
                                conversion = monto * MonedaObject.get("COP").getAsDouble();
                            case 4:
                                conversion = monto * MonedaObject.get("MXN").getAsDouble();
                            case 5, 6, 7, 8:
                                conversion = monto * MonedaObject.get("USD").getAsDouble();
                        }
                        System.out.println("Su conversión de moneda es: " + conversion);
                    } else {
                        System.out.println("No tenemos esas tasas de conversión");
                    }
                } else {
                    System.out.println("Lo siento, no se pudieron obtener datos de conversión");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
