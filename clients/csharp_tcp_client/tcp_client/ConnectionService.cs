using System.Net.Sockets;

namespace tcp_client;
public class ConnectionService
{
    private StreamReader? inStream;
    private StreamWriter? outStream;
    private Socket? conn;
    private static string? ip;

    public string SendAndReceiveMessage(string message)
    {
        try
        {
            conn = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            conn.Connect(ip!, 65000);
            Console.WriteLine("Service connected.");

            SetOut();
            SetIn();

            outStream!.WriteLine(message);

            Console.WriteLine("Waiting response...");
            string response = inStream!.ReadLine()!;
            Console.WriteLine("Response: " + response);
            return response;
        }
        catch (Exception e)
        {
            Console.WriteLine("Error");
            return e.Message;
        }
        finally
        {
            if (conn != null)
            {
                conn.Close();
                Console.WriteLine("Socket closed.");
            }
        }
    }

    public void SetIn()
    {
        inStream = new StreamReader(new NetworkStream(conn!));
    }

    public void SetOut()
    {
        outStream = new StreamWriter(new NetworkStream(conn!)) { AutoFlush = true };
    }

    public static void SetIP(string ip)
    {
        ConnectionService.ip = ip;
    }
}