package main

import (
	"bufio"
	"fmt"
	"net"
)

type ConnectionService struct {
	ip   string
	conn net.Conn
	in   *bufio.Reader
	out  *bufio.Writer
}

func (c *ConnectionService) setIn() {
	c.in = bufio.NewReader(c.conn)
}

func (c *ConnectionService) setOut() {
	c.out = bufio.NewWriter(c.conn)
}

func (c *ConnectionService) SendAndReceiveMessage(message string) (string, error) {
	var err error
	c.conn, err = net.Dial("tcp", fmt.Sprintf("%s:%d", c.ip, 65000))
	if err != nil {
		fmt.Println("Error connecting:", err)
		return "", err
	}
	defer c.conn.Close()
	fmt.Println("Service connected.")

	c.setOut()
	c.setIn()

	_, err = c.out.WriteString(message + "\n")
	if err != nil {
		fmt.Println("Error sending message:", err)
		return "", err
	}
	c.out.Flush()

	fmt.Println("Waiting response...")
	response, err := c.in.ReadString('\n')
	if err != nil {
		fmt.Println("Error reading response:", err)
		return "", err
	}
	return response, nil
}

func main() {
	connectionService := &ConnectionService{ip: "localhost"}
	response, err := connectionService.SendAndReceiveMessage("2; estou programando como um hamster ligeiro!")
	if err != nil {
		fmt.Println("Error:", err)
	} else {
		fmt.Println("Received:", response)
	}
}
