use std::io::{self, BufRead, BufReader, Write};
use std::net::TcpStream;

struct ConnectionService {
    ip: String,
}

impl ConnectionService {
    fn new(ip: String) -> Self {
        ConnectionService { ip }
    }

    fn send_and_receive_message(&self, message: &str) -> io::Result<String> {
        let address = format!("{}:{}", self.ip, 65000);
        let stream = TcpStream::connect(&address)?;
        println!("Service connected.");

        let mut reader = BufReader::new(stream.try_clone()?);
        let mut writer = stream.try_clone()?;
        
        writeln!(writer, "{}", message)?;
        
        println!("Waiting response...");
        let mut response = String::new();
        reader.read_line(&mut response)?;
        Ok(response.trim().to_string())
    }
}

fn main() {
    let connection_service = ConnectionService::new("localhost".to_string());
    match connection_service.send_and_receive_message(
        "5; estou programando como um carangueijo (que não está enferrujado!)."
    ) {
        Ok(response) => println!("Received: {}", response),
        Err(e) => eprintln!("Error: {}", e),
    }
}