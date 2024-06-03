const net = require('net');

class ConnectionService {
    constructor() {
        this.ip = '';
        this.conn = null;
        this.in = null;
        this.out = null;
    }

    setIP(ip) {
        this.ip = ip;
    }

    async sendAndReceiveMessage(message) {
        return new Promise((resolve, reject) => {
            this.conn = net.createConnection({ host: this.ip, port: 65000 }, () => {
                console.log('Service connected.');

                this.conn.write(message + '\n');

                this.conn.on('data', (data) => {
                    const response = data.toString();
                    this.conn.end();
                    resolve(response);
                });

                this.conn.on('error', (err) => {
                    console.log('Error');
                    reject(err.message);
                });

                this.conn.on('end', () => {
                    console.log('Socket closed.');
                });
            });
        });
    }
}

(async () => {
    const connectionService = new ConnectionService();
    connectionService.setIP('localhost');
    try {
        const response = await connectionService.sendAndReceiveMessage('3; estou programando como um mutante!');
        console.log('Received:', response);
    } catch (err) {
        console.log('Error:', err);
    }
})();