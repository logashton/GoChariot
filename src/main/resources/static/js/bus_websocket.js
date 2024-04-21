const wsUrl = 'wss://passio3.com/';
let buses = {};
let map;

async function addMarkerPopup(id) {
    buses[id].addListener('click', async () => {
        let busInfo = await fetch('/api/bus/id/' + id);
        busInfo = await busInfo.json();

        const contentString = `
                <div>
                    <h3>Bus: ${busInfo.theBus.busId}</h3>
                    <p>Driver: ${busInfo.theBus.driver}</p>
                    <p>speed: ${busInfo.theBus.speed}</p>
                    <p>PAX Load: ${busInfo.theBus.paxLoadS}</p>
                </div>
            `;

        const infoWindow = new google.maps.InfoWindow({
            content: contentString,
        });

        infoWindow.open(map, buses[id]);

    });
}

async function initMap() {
    // credit to github.com/salimkayabasi for animateTo method
    google.maps.Marker.prototype.animateTo = function (newPosition, options) {
        defaultOptions = {
            duration: 1000,
            easing: 'linear',
            complete: null
        }
        options = options || {};

        // complete missing options
        for (key in defaultOptions) {
            options[key] = options[key] || defaultOptions[key];
        }

        // throw exception if easing function doesn't exist
        if (options.easing != 'linear') {
            if (typeof jQuery == 'undefined' || !jQuery.easing[options.easing]) {
                throw '"' + options.easing + '" easing function doesn\'t exist. Include jQuery and/or the jQuery easing plugin and use the right function name.';
                return;
            }
        }

        window.requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;
        window.cancelAnimationFrame = window.cancelAnimationFrame || window.mozCancelAnimationFrame;

        // save current position. prefixed to avoid name collisions. separate for lat/lng to avoid calling lat()/lng() in every frame
        this.AT_startPosition_lat = this.getPosition().lat();
        this.AT_startPosition_lng = this.getPosition().lng();
        var newPosition_lat = newPosition.lat();
        var newPosition_lng = newPosition.lng();

        // crossing the 180° meridian and going the long way around the earth?
        if (Math.abs(newPosition_lng - this.AT_startPosition_lng) > 180) {
            if (newPosition_lng > this.AT_startPosition_lng) {
                newPosition_lng -= 360;
            } else {
                newPosition_lng += 360;
            }
        }

        var animateStep = function (marker, startTime) {
            var ellapsedTime = (new Date()).getTime() - startTime;
            var durationRatio = ellapsedTime / options.duration; // 0 - 1
            var easingDurationRatio = durationRatio;

            // use jQuery easing if it's not linear
            if (options.easing !== 'linear') {
                easingDurationRatio = jQuery.easing[options.easing](durationRatio, ellapsedTime, 0, 1, options.duration);
            }

            if (durationRatio < 1) {
                var deltaPosition = new google.maps.LatLng(marker.AT_startPosition_lat + (newPosition_lat - marker.AT_startPosition_lat) * easingDurationRatio,
                    marker.AT_startPosition_lng + (newPosition_lng - marker.AT_startPosition_lng) * easingDurationRatio);
                marker.setPosition(deltaPosition);

                // use requestAnimationFrame if it exists on this browser. If not, use setTimeout with ~60 fps
                if (window.requestAnimationFrame) {
                    marker.AT_animationHandler = window.requestAnimationFrame(function () {
                        animateStep(marker, startTime)
                    });
                } else {
                    marker.AT_animationHandler = setTimeout(function () {
                        animateStep(marker, startTime)
                    }, 17);
                }

            } else {

                marker.setPosition(newPosition);

                if (typeof options.complete === 'function') {
                    options.complete();
                }

            }
        }

        // stop possibly running animation
        if (window.cancelAnimationFrame) {
            window.cancelAnimationFrame(this.AT_animationHandler);
        } else {
            clearTimeout(this.AT_animationHandler);
        }

        animateStep(this, (new Date()).getTime());
    }

    let center = {lat: 36.06865390159834, lng: -79.8107065341785};
    map = new google.maps.Map(document.getElementById('map'), {
        center: center,
        zoom: 15,
        options: {
            gestureHandling: 'greedy',
            useStaticMap: false
        },
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.DROPDOWN_MENU,
            mapTypeIds: ['roadmap', 'satellite', 'hybrid', 'terrain','simplified_map','osm']
        },
        zoomControl: true,
        zoomControlOptions: {
            position: google.maps.ControlPosition.LEFT_CENTER,
        },
        streetViewControl: false
    });

    let loadingOverlay = new google.maps.InfoWindow({
        content: 'Loading Bus Locations...',
        position: map.getCenter(),
    });
    loadingOverlay.open(map);

    let fetchedBuses = await getBuses(true);

    for (const [busId, busInfo] of Object.entries(fetchedBuses)) {
        console.log('creating new pin frmo init', busId, busInfo);
        buses[busId] = new google.maps.Marker({
            position: {lat: busInfo.latitude, lng: busInfo.longitude},
            map: map,
            shape: {
                type: 'circle',
                coords: [0, 0, 60]
            },
            icon: {
                url: '/images/favicon.png',
                scaledSize: new google.maps.Size(32, 32)
            },
            title: 'Bus: ' + busInfo.busId,
        });

        addMarkerPopup(busId);
    }

    setTimeout(() => {
        loadingOverlay.close();
        loadingOverlay = null;
    }, 1555);


}

function updatePin(id, latitude, longitude, course, pax) {
    let newLatLng = new google.maps.LatLng(latitude, longitude);

    if (id in buses) {
        let distance = google.maps.geometry.spherical.computeDistanceBetween(new google.maps.LatLng(buses[id].getPosition()), new google.maps.LatLng(newLatLng));
        distance<10 || distance>300 ? buses[id].setPosition(newLatLng) : buses[id].animateTo(newLatLng,{duration: 9000});
    } else {
        console.log('trying to create new pin');
        buses[id] = new google.maps.Marker({
            position: newLatLng,
            map: map,
            shape: {
                type: 'circle',
                coords: [0, 0, 60]
            },
            animation: google.maps.Animation.DROP,
            icon: {
                url: "/images/favicon.png",
                scaledSize: new google.maps.Size(32, 32)
            },
            title: 'Bus: ' + id,
        });

        addMarkerPopup(id);


    }

}

async function getBuses(detailed = false) {
    try {
        const response = await fetch('/api/bus/all');

        if (!response.ok) {
            throw new Error(`error: ${response.status}`);
        }

        const data = await response.json();

        if (detailed) {
            return data.buses;
        }

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
            let eventData = JSON.parse(event.data);
            console.log(eventData.busId);
            updatePin(
                eventData.busId,
                eventData.latitude,
                eventData.longitude,
                eventData.course,
                eventData.paxLoad
            );
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

function loadScript() {
    let script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCmmCmJxrpgZNdbad86zGFxi6nn3g3jt4w&callback=initMap&libraries=geometry';
    document.body.appendChild(script);
}

window.onload = loadScript;




