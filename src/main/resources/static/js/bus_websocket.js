const wsUrl = 'wss://passio3.com/';
// bus markers <id, map marker>
let buses = {};
let map;
let circle_big_w_arrow;
let busIconDefImg;
let Canvas;
let Ctx;
let tintCanvas;
let tintCtx;
// stop markers <id, map marker>
let stopsMarkers = {};
let routePaths = [];
let stopsToRoutes = {};
let userMarker;

function updateLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            const userLocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude,
            };

            const heading = position.coords.heading;

            if (!userMarker) {
                userMarker = new google.maps.Marker({
                    position: userLocation,
                    map: map,
                    icon: {
                        path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                        scale: 5,
                        fillColor: "#007AFF",
                        fillOpacity: 0.75,
                        strokeWeight: 2,
                        strokeColor: "#fbfffa",
                        strokeOpacity: 0.75,
                        rotation: heading,
                    },
                });
            } else {
                userMarker.setPosition(userLocation);
                userMarker.setIcon({
                    path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                    scale: 5,
                    fillColor: "#007aff",
                    fillOpacity: 0.75,
                    strokeColor: "#fbfffa",
                    strokeOpacity: 0.75,
                    strokeWeight: 2,
                    rotation: heading,
                });
            }

        });
    } else {
        alert("Geolocation is not supported by this browser.");
    }
}


async function findBusStops() {
    let polyLines = await fetch('/api/bus/stops');
    return await polyLines.json();
}

function calculateAverageRating(rating) {
    return '★★★★★'.substring(0, Math.round(rating));
}

async function isDriverVerified(driverIdPGO) {
    let verified = await fetch('/api/bus/verified/id/' + driverIdPGO);
    verified = await verified.json();
    console.log('is driver verified: ', verified);
    return verified;

}


function redirectToReviews(driverName) {
    window.location.href = `review?driverId=${encodeURIComponent(driverName)}`;
}

async function createStopRoutesMap() {
    let data = await fetch('/api/transits?path=lines');
    data = await data.json();
    for (const [id, stopInfo] of Object.entries(data)) {
        stopInfo.stations.forEach(station => {
            if (stopsToRoutes.hasOwnProperty(station)) {
                stopsToRoutes[station].push(id);
            } else {
                stopsToRoutes[station] = [id];
            }
        });
    }
}

async function initPolyLines() {
    let lines = await findBusStops();

    for (const [routeId, routePath] of Object.entries(lines.routePoints)) {
        let targetRoute = lines.routes[routeId];
        let points = routePath[0].map((point) => {
            return new google.maps.LatLng(point.lat, point.lng);
        });
        drawRoutePointsPart(
            points,
            false,
            targetRoute[1],
            routeId,
            '123',
            targetRoute[0]
        );
    }

}

// thanks passiogo
function drawBusIconAndMarker(busname, buscolor, calculatedCourse, paxLoad){
    return rotateColorBusMarkerCircleBig(busname,buscolor,calculatedCourse,paxLoad);
}

function rotateColorBusMarkerCircleBig(busname, buscolor, calculatedCourse, paxLoad){
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

function getRouteArrow(routeColor, opacity){
    return {
        path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
        scale:4,
        strokeColor:"#ffffff",
        strokeWeight:0.5,
        fillColor:routeColor,
        fillOpacity:opacity*0.6
    }
}

function drawRoutePointsPart(points,outdated,routeColor,routeId,routeGroupId, routeName){
    console.log('IN DRAW ROUTE', routeColor, routeId, routeName);
    //console.log("drawRoutePointsPart(routeId="+routeId+"): outdated="+outdated+", routeColor="+routeColor)
    if (!points || !points.length) return;
    //let opacity=getRouteOpacity({routeId:routeId,routeGroupId:routeGroupId})*(outdated?0.75:1.0);//outdated returned 01122021, commented //24092021djd
    //let strokeWeight=opacity==newRouteUIdimmedOpacity ? newRouteUIdimmedWeight : 5;
    let opacity = 0.7;
    let strokeWeight = 1;
    //console.log("drawRoutePointsPart(routeId="+routeId+"), opacity="+opacity+", strokeWeight="+strokeWeight);
    if (points.length>0){
        let flightPath = new google.maps.Polyline({
            path: points,
            visible:true,
            geodesic: false,
            clickable:false,
            strokeColor: routeColor,
            strokeOpacity: opacity,
            strokeWeight: strokeWeight,
            routeId: routeId,
            routeName: routeName,
            routeGroupId:routeGroupId,
            icons: [{
                icon:getRouteArrow(routeColor, opacity),
                repeat:'200px',
                path:[]
            }]
        });
        flightPath.setMap(map);
        routePaths.push(flightPath);
        //console.log("drawStopMarkers: added routeId="+routePaths[routePaths.length-1].routeId);
    }
}



async function stopMarkerPopup(stopId) {
    let routes = await fetch('/api/transits?path=lines');
    routes = await routes.json();

    //let routes
    let contentString = `
    <div style="font-size: 18px; width:auto; max-width: 800px; max-height:400; overflow-x:hidden;">
        <span><img src="/images/roadstop.png"></span><div style="display: inline; font-size: 16px; font-weight: bold;">${stopsMarkers[stopId].stopName}</div>
        <hr class="yellow">
        <div style="display: flex; flex-direction: column; align-items: flex-start;">
    `;

    stopsToRoutes[stopId].forEach(routeId => {
        contentString += `
            <div>
                ${routes[routeId].lineNameLong}
            </div>
        `
    })

    contentString += '</div></div>'

    console.log('stop clciked');


    const infoWindow = new google.maps.InfoWindow({
        content: contentString,
    });
    infoWindow.open(map, stopsMarkers[stopId]);
}

async function requestModal(routeName, driverName, driverIdPGO) {
    let stops = await findBusStops();
    document.getElementById('route').value = routeName;
    document.getElementById('driver').value = driverName;
    document.getElementById('driverIdPGO').value = driverIdPGO;

    console.log('open modal received: ', routeName, driverName, driverIdPGO);

    const optionsData = [];

    for (const [stopId, stop] of Object.entries(stops.stops)) {
        if (stop.routeName == routeName) {
            optionsData.push(stop.name);
        }
    }



    const selectElement = document.getElementById('pickUp');
    const selectElementDrop = document.getElementById('dropOff');
    optionsData.forEach(option => {
        const optionElement = document.createElement('option');
        optionElement.value = option;
        optionElement.textContent = option;
        selectElement.appendChild(optionElement);
        selectElementDrop.appendChild(optionElement.cloneNode(true));
    });

    document.getElementById('rideRequestModal').style.display='block';

}

async function addMarkerPopup(id) {
    buses[id].addListener('click', async () => {
        let busInfo = await fetch('/api/bus/id/' + id);
        busInfo = await busInfo.json();


        console.log('the bus: '+ busInfo.theBus);
        console.log(busInfo);

        let driverInfo = await fetch(
            '/api/reviews/average?driverId=' + busInfo.theBus.driverId
        )

        let reviewRating = "No reviews";

        console.log(driverInfo);
        if (driverInfo.status != 404) {
            reviewRating = calculateAverageRating(await driverInfo.text());
        }

        let verified = await isDriverVerified(busInfo.theBus.driverId);
        // ${verified ? '' : 'disabled'}

        console.log(`is ${busInfo.theBus.driver} verified: `, verified);

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
            <p>Driver: ${busInfo.theBus.driver}  ${verified ? '<img src="/images/verified.png" alt="Verified" style="width: 20px; height: 20px;">' : ''} (${reviewRating})</p>
            <p>Speed: ${Math.floor(busInfo.theBus.speed)} mph</p>
            <p>Load: ${busInfo.theBus.paxLoadS}</p>
            <button onclick="requestModal('${busInfo.theBus.routeName}', '${busInfo.theBus.driver}', '${busInfo.theBus.driverId}')" ${verified ? '' : 'disabled class="btn btn-secondary"'}>${verified ? 'Request ride' : "Can't request ride"}</button>
            <button onclick="redirectToReviews('${busInfo.theBus.driverId}')">View Reviews</button>
        </div>
    </div>
    `;




        const infoWindow = new google.maps.InfoWindow({
            content: contentString,
        });

        infoWindow.open(map, buses[id]);

    });
}


function highlightRoute(routeIds) {
    routePaths.forEach(path => {
        if (routeIds.includes(path.routeId)) {
            path.setOptions({strokeOpacity: 1.0});
            path.setOptions({strokeWeight: 5});
            setTimeout(() => {
                path.setOptions({strokeOpacity: 0.7});
                path.setOptions({strokeWeight: 1});
            }, 3500)
        } else {
            path.setOptions({strokeOpacity: 0.7});
            path.setOptions({strokeWeight: 1});
        }
    });
}

async function addStops() {
    let stops = await findBusStops();
    console.log('stops', stops.stops);

    for (const [stopId, stop] of Object.entries(stops.stops)) {
        // bruh
        let fillColor = stops.routes[stop.routeId][1];
        let strokeColor = stops.routes[stop.routeId][1];
        if (stopsToRoutes[stop.id].length > 1) {
            strokeColor = stops.routes[stopsToRoutes[stop.id][0]][1]
        }
        console.log('ummm', stopId, stop, stop.id);
        stopsMarkers[stop.id] = new google.maps.Marker({
            position: new google.maps.LatLng(stop.latitude, stop.longitude),
            icon: {
                //path: 'M-5,0a5,5 0 1,0 10,0a5,5 0 1,0 -10,0',
                path: 'M -5,0 A 5,5 0 0,1 5,0 A 5,5 0 1,1 -5,0 Z',
                scale: 1.25, //25012024 :7,
                //fillColor: "#FFFFFF",
                fillColor: fillColor,
                fillOpacity: 1,
                strokeColor: strokeColor,
                //strokeColor: 'transparent',
                strokeOpacity: 1,
                strokeWeight:2.5,
                labelOrigin: new google.maps.Point(0, 2)
            },
            stopId: stop.stopId,
            stopName: stop.name,
            id: stop.stopId,
            routeIds: stopsToRoutes[stop.id],
            title: stop.name,
            label:{text:" ",color: "#0000ffaa",fontSize: "11px"},
        });

        stopsMarkers[stop.id].setMap(map);
        stopsMarkers[stop.id].addListener('click', async () => await stopMarkerPopup(stop.id));
        stopsMarkers[stop.id].addListener('mouseover', () => highlightRoute(stopsMarkers[stop.id].routeIds));

    }
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

    google.maps.Polyline.prototype.getBounds = function() {
        var bounds = new google.maps.LatLngBounds();
        this.getPath().forEach(function(item, index) {
            bounds.extend(new google.maps.LatLng(item.lat(), item.lng()));
        });
        return bounds;
    };

    let center = {lat: 36.06865390159834, lng: -79.8107065341785};
    map = new google.maps.Map(document.getElementById('map'), {
        center: center,
        zoom: 15.5,
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

    updateLocation();
    setInterval(updateLocation, 5000);
    await createStopRoutesMap();
    console.log('VALUE AFTER STOP ROUTES:', stopsToRoutes);
    await addMarkers();
    await addStops();
    await initPolyLines();

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
        //console.log('icon properties in update:', id,buses[id].color,course,pax);
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
            //console.log('Message from server:', event.data);
            let eventData = JSON.parse(event.data);
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

setTimeout(() => {
    //requestModal("Campus Loop", 'J. Knight', '123123');
}, 5555);

fetchDataAndConnect();





