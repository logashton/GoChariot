const wsUrl = 'wss://passio3.com/';
let buses = {};
let map;
let circle_big_w_arrow;
let busIconDefImg;
let Canvas;
let Ctx;
let tintCanvas;
let tintCtx;


const calculateAverageRating = (rating) => {
    return '★★★★★'.substring(0, Math.round(rating));
};


function redirectToReviews(driverName) {
    window.location.href = `review?driverName=${encodeURIComponent(driverName)}`;
}

// thanks passiogo
function drawBusIconAndMarker(busname,buscolor,calculatedCourse,paxLoad){
    return rotateColorBusMarkerCircleBig(busname,buscolor,calculatedCourse,paxLoad);
}
function rotateColorBusMarkerCircleBig(busname,buscolor,calculatedCourse,paxLoad){
    if (!circle_big_w_arrow) {
        Canvas = $("#canvas");
        Ctx = Canvas[0].getContext("2d");
        tintCanvas = $("#tintCanvas");
        console.log('ctx: ' + Ctx);
        tintCtx = tintCanvas[0].getContext('2d');
        circle_big_w_arrow = new Image(Canvas[0].width, Canvas[0].height);
        circle_big_w_arrow.src = $("#circle_big_w_arrow").attr("src");
        console.log(" Canvas.width=" + Canvas[0].width + ", Canvas.height=" + Canvas[0].height + ', src=' + $("#circle_big_w_arrow").attr("src") + ', img w/h=' + circle_big_w_arrow.width + '/' + circle_big_w_arrow.height);
    }

    if (!busIconDefImg) {
        busIconDefImg = new Image(Canvas[0].width * 0.33, Canvas[0].height * 0.33)
        busIconDefImg.src = $("#busIconDefImg").attr("src");
    }

    Ctx.clearRect(0, 0, 60, 60);

    tintCtx.clearRect(0, 0, 60, 60);
    tintCtx.save();
    tintCtx.translate(tintCanvas[0].width/2,tintCanvas[0].height/2);
    let alpha=(calculatedCourse-90.0-90.0)*2.0*Math.PI/365.0;
    tintCtx.rotate(alpha);
    tintCtx.fillStyle = buscolor;
    tintCtx.imageSmoothingEnabled = true;
    tintCtx.fillRect(-tintCanvas[0].width/2,-tintCanvas[0].height/2,tintCanvas[0].width,tintCanvas[0].height);
    tintCtx.globalCompositeOperation = "destination-atop";
    tintCtx.drawImage(circle_big_w_arrow, -tintCanvas[0].width/2,-tintCanvas[0].height/2,tintCanvas[0].width,tintCanvas[0].height);
    tintCtx.globalCompositeOperation = "source-over";
    tintCtx.restore();
    Ctx.drawImage(tintCanvas[0],0,0);

    var circleRadius=Canvas[0].width*(0.3-(+paxLoad/1000));
    var arcX=Canvas[0].width/2;
    var arcY=Canvas[0].height/2;
    Ctx.beginPath();
    Ctx.arc(arcX,arcY,circleRadius, 0, 2 * Math.PI, false);
    Ctx.fillStyle = 'white';
    Ctx.fill();


    tintCtx.clearRect(0, 0, 60, 60);
    tintCtx.fillStyle = buscolor;
    tintCtx.fillRect(0,0,tintCanvas[0].width,tintCanvas[0].height);
    tintCtx.globalCompositeOperation = "destination-atop";
    tintCtx.drawImage(busIconDefImg, 0,0,busIconDefImg.width,busIconDefImg.height);
    tintCtx.globalCompositeOperation = "source-over";
    Ctx.drawImage(tintCanvas[0],(Canvas[0].width-busIconDefImg.width)/2,(Canvas[0].height-busIconDefImg.height)/2);

    return [Canvas[0].toDataURL("image/png"),Canvas[0].width/2,Canvas[0].height/2];
}

async function addMarkerPopup(id) {
    buses[id].addListener('click', async () => {
        let busInfo = await fetch('/api/bus/id/' + id);
        busInfo = await busInfo.json();

        let driverInfo = await fetch(
            '/api/reviews/average?driverName=' + busInfo.theBus.driver
        )

        let reviewRating = "No reviews";

        console.log(driverInfo);
        if (driverInfo.status != 404) {
            reviewRating = calculateAverageRating(await driverInfo.text());
        }

        const contentString = `
    <div style="font-size: 18px; width:auto; max-width: 800px; max-height:400; overflow-x:hidden;">
        <div style="display: flex; flex-direction: column; align-items: flex-start;">
            <div style="display: flex; align-items: center;">
                <img src="/images/bus_white.png" alt="" style="width: 30px; height: 30px; background: ${busInfo.theBus.color}; border-radius: 50%;">
                <span style="font-weight: bold; margin-left: 10px;">${busInfo.theBus.name}</span>
            </div>
        </div>
        <div style="display: flex; flex-direction: column; padding-left: 10px;">
            <p>Route: ${busInfo.theBus.routeName}</p>
            <p>Driver: ${busInfo.theBus.driver} (${reviewRating})</p>
            <p>Speed: ${Math.floor(busInfo.theBus.speed)} mph</p>
            <p>Load: ${busInfo.theBus.paxLoadS}</p>
            <button onclick="">Request ride</button>
            <button onclick="redirectToReviews('${busInfo.theBus.driver}', '${busInfo.theBus.driverId}')">View Reviews</button>
        </div>
    </div>
    `;




        const infoWindow = new google.maps.InfoWindow({
            content: contentString,
        });

        infoWindow.open(map, buses[id]);

    });
}

async function addMarkers() {
    let loadingOverlay = new google.maps.InfoWindow({
        content: 'Loading Bus Locations...',
        position: map.getCenter(),
    });
    loadingOverlay.open(map);

    let fetchedBuses = await getBuses(true);
    for (const [busId, busInfo] of Object.entries(fetchedBuses)) {
        console.log('creating new pin frmo init', busId, busInfo);
        console.log('BUS INFO: ', busInfo[0]);
        console.log('THE BUSES ID: ' + busInfo[0].busId);
        buses[busInfo[0].busId] = new google.maps.Marker({
            position: new google.maps.LatLng(busInfo[0].latitude, busInfo[0].longitude),
            map: map,
            shape: {
                type: 'circle',
                coords: [0, 0, 60]
            },
            icon: {
                url: '/images/favicon.png',
                scaledSize: new google.maps.Size(27, 27),
            },
            title: 'Bus: ' + busInfo[0].busId,
        });
        addMarkerPopup(busInfo[0].busId);
        buses[busInfo[0].busId].color = busInfo[0].color;
    }

    loadingOverlay.close();
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

    await addMarkers();

    let styles = [
        {
            "featureType": "poi.business",
            "elementType": "labels",
            "stylers": [
                {"visibility": "off"}
            ]
        },
        {
            "featureType": "poi.park",
            "elementType": "labels",
            "stylers": [
                {"visibility": "off"}
            ]
        }
    ];

    map.setOptions({styles: styles});


}

async function updatePin(id, latitude, longitude, course, pax) {
    let newLatLng = new google.maps.LatLng(latitude, longitude);
    let myIcon= {
        size: new google.maps.Size(44,44),
        scaledSize: new google.maps.Size(44, 44)
    };

    if (id in buses) {
        let distance = google.maps.geometry.spherical.computeDistanceBetween(new google.maps.LatLng(buses[id].getPosition()), new google.maps.LatLng(newLatLng));
        distance<10 || distance>300 ? buses[id].setPosition(newLatLng) : buses[id].animateTo(newLatLng,{duration: 9000});
        let ico = await drawBusIconAndMarker(id,buses[id].color,course,pax);
        console.log('icon properties in update:', id,buses[id].color,course,pax);
        myIcon.url=ico[0]
        myIcon.anchor=new google.maps.Point(ico[1],ico[2])
        buses[id].setIcon(myIcon);
    } else {
        console.log('trying to create new pin');
        console.log(buses);
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

function loadScript() {
    let script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyCmmCmJxrpgZNdbad86zGFxi6nn3g3jt4w&callback=initMap&libraries=geometry';
    document.body.appendChild(script);
}

window.onload = loadScript;

fetchDataAndConnect();





