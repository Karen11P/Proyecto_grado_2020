#include <FirebaseArduino.h>
#include <ESP8266WiFi.h>
#include <String.h>
#include <ListLib.h>

#define FIREBASE_HOST "proyecto-grado-usta2019.firebaseio.com"
#define FIREBASE_AUTH "d1yrOKR5xIeckyTbY3Sd5I0QcQkShP7PkRnd222W"
#define WIFI_SSID "CMORENO"
#define WIFI_PASSWORD "agatha97"

char *strtok( char *str1, const char *str2 );
List<String> lista_ids;
List<bool> lista_estados;

void setup()
{
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  delay(10);
  Serial.print("connecting.");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.println("connected");
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  delay(1000);
  Serial.println("Firebase");
}

void loop()
{
  String lista_switch = Firebase.getString("joyce3101/lista_switch");
  Serial.println(lista_switch);
  char str[100];
  lista_switch.toCharArray(str, 100);
  char delimitadores[] = ",";
  char *resultado = NULL;
  if (lista_switch != "")
  {
    resultado = strtok( str, delimitadores );
    while ( resultado != NULL )
    {
      String ID = String(resultado);
      String direccion = "joyce3101/switch/"+ID+"/Estado";
      boolean estado = Firebase.getBool(direccion);
      resultado = strtok( NULL, delimitadores );
      if (lista_ids.Contains(ID) == false)
      {
        lista_ids.Add(ID);
        lista_estados.Add(estado);
        Enviar(ID,estado);
      }
      else
      {
        int pos = lista_ids.IndexOf(ID);
        if(estado != lista_estados[pos])
        {          
          lista_estados[pos] = estado;
          Enviar(ID,estado);
        }
      }
    }
  }
  delay(1000);
}

void Enviar(String ID, boolean estado)
{
  String mensaje = ID+"159"+estado;
  Serial.println(mensaje);
}
