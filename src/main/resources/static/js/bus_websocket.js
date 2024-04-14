const wsUrl = 'wss://passio3.com/';

async function getBuses() {
    try {
        const response = await fetch('/api/bus/all');

        if (!response.ok) {
            throw new Error(`error: ${response.status}`);
        }

        const data = await response.json();
        return Object.values(data.buses).flat().map(bus => bus.busId);

    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

async function fetchDataAndConnect() {
    try {
        const busIds = await getBuses();
        console.log('BUS IDS FETCHED FROM API: ' + busIds);

        const subscriptionMessage = JSON.stringify({
            "subscribe": "location",
            "userId": [2874],
            "filter": {
                "outOfService": 0,
                "busId": busIds
            },
            "field": ["busId", "latitude", "longitude", "course", "paxLoad", "more"]
        });

        const socket = new WebSocket(wsUrl);

        socket.addEventListener('open', (event) => {
            console.log('WebSocket connection opened');
            socket.send(subscriptionMessage);
        });

        socket.addEventListener('message', (event) => {
            console.log('Message from server:', event.data);
        });

        socket.addEventListener('error', (error) => {
            console.error('WebSocket Error:', error);
        });

        socket.addEventListener('close', (event) => {
            console.log('WebSocket connection closed:', event.code, event.reason);
        });

    } catch (error) {
        console.error('Error:', error);
    }
}

fetchDataAndConnect();
