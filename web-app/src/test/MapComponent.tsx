// import React, { useState } from 'react';
// import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet';
// import L from 'leaflet';
// import 'leaflet/dist/leaflet.-

// import React, { useState, useEffect } from 'react';
// import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
// import L from 'leaflet';
// import 'leaflet/dist/leaflet.css';
// import 'leaflet-control-geocoder/dist/Control.Geocoder.css'; // CSS geocoder
// import * as LCG from 'leaflet-control-geocoder'; // Plugin geocoder

// interface LatLng {
//     latitude: number;
//     longitude: number;
// }

// const LocationMarker: React.FC<{ position: LatLng }> = ({ position }) => {
//     const defaultIcon = new L.Icon({
//         iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
//         iconRetinaUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png',
//         shadowUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
//         iconSize: [25, 41],
//         iconAnchor: [12, 41],
//         popupAnchor: [1, -34],
//         tooltipAnchor: [16, -28],
//     });

//     return (
//         <Marker position={[position.latitude, position.longitude]} icon={defaultIcon}>
//             <Popup>
//                 Selected Location: {position.latitude}, {position.longitude}
//             </Popup>
//         </Marker>
//     );
// };

// const MapComponent: React.FC = () => {
//     const [location, setLocation] = useState<LatLng | null>(null);

//     // Chuyển hook useMap vào trong MapContainer
//     return (
//         <div>
//             <h3>Search for a location</h3>
//             <MapContainer
//                 center={[51.505, -0.09]} // Default center
//                 zoom={13} // Default zoom
//                 style={{ height: '500px', width: '100%' }}
//             >
//                 <TileLayer
//                     url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
//                     attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
//                 />
//                 <LocationSearch setLocation={setLocation} />
//                 {location && <LocationMarker position={location} />}
//             </MapContainer>

//             {location && (
//                 <div style={{ marginTop: '10px' }}>
//                     <p>Selected Location:</p>
//                     <p>Latitude: {location.latitude}</p>
//                     <p>Longitude: {location.longitude}</p>
//                 </div>
//             )}
//         </div>
//     );
// };

// const LocationSearch: React.FC<{ setLocation: (location: LatLng) => void }> = ({
//     setLocation,
// }) => {
//     const map = useMap(); // Sử dụng hook useMap trong phạm vi của MapContainer

//     useEffect(() => {
//         const geocoderControl = LCG.geocoder().on('markgeocode', function (e: any) {
//             const latLng = {
//                 latitude: e.geocode.center.lat,
//                 longitude: e.geocode.center.lng,
//             };
//             setLocation(latLng); // Cập nhật vị trí khi tìm kiếm
//             map.setView([latLng.latitude, latLng.longitude], 15); // Di chuyển bản đồ đến vị trí mới
//         });

//         geocoderControl.addTo(map);
//     }, [map, setLocation]);

//     return null; // Không cần render gì ở đây
// };

// export default MapComponent;

// lấy thông tin dựa trên kinh độ vĩ độ

import React, { useState } from 'react';
import { MapContainer, TileLayer, Marker, useMapEvents, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

const customIcon = new L.Icon({
    iconUrl:
        'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
});

const ReverseGeocode = () => {
    const [address, setAddress] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [markerPosition, setMarkerPosition] = useState<{
        lat: number;
        lng: number;
    } | null>(null);
    const [searchQuery, setSearchQuery] = useState<string>('');
    const [suggestions, setSuggestions] = useState<any[]>([]);

    const getAddress = async (latitude: number, longitude: number) => {
        try {
            const response = await fetch(
                `https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`
            );
            const data = await response.json();

            if (data && data.address) {
                console.log(data.address);

                // Tạo chuỗi địa chỉ chỉ với các giá trị
                const addressString = Object.values(data.address)
                    .map((value) => value || 'N/A') // Thay thế giá trị null/undefined bằng 'N/A'
                    .join(', '); // Ghép các giá trị lại với nhau, cách nhau bằng dấu phẩy

                // Cập nhật state với chuỗi địa chỉ
                setAddress(addressString);
            } else {
                setError('Không tìm thấy địa chỉ cho vị trí này.');
            }
        } catch (error) {
            setError('Đã có lỗi xảy ra.');
        }
    };

    const searchLocation = async () => {
        if (searchQuery.length > 2) {
            try {
                const response = await fetch(
                    `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(
                        searchQuery
                    )}&format=json&addressdetails=1&limit=5`
                );
                const data = await response.json();
                setSuggestions(data);
            } catch (error) {
                setError('Đã có lỗi xảy ra khi tìm kiếm.');
            }
        }
    };

    const handleSuggestionClick = (suggestion: any) => {
        const { lat, lon } = suggestion;
        const latitude = parseFloat(lat);
        const longitude = parseFloat(lon);
        setMarkerPosition({ lat: latitude, lng: longitude });
        getAddress(latitude, longitude);
        setSuggestions([]); // Clear suggestions after selection
        setSearchQuery(suggestion.display_name); // Update input with selected suggestion
    };

    const MapController = ({
        position,
    }: {
        position: { lat: number; lng: number } | null;
    }) => {
        const map = useMap();

        if (position) {
            map.setView([position.lat, position.lng], map.getZoom(), {
                animate: true,
            });
        }

        return null;
    };

    const MapComponent = () => {
        useMapEvents({
            click: (event) => {
                const { lat, lng } = event.latlng;
                setMarkerPosition({ lat, lng });
                getAddress(lat, lng);
            },
        });

        return null;
    };

    return (
        <div>
            <h3>Thông tin địa lý từ kinh độ và vĩ độ</h3>
            <div style={{ marginBottom: '10px' }}>
                <input
                    type="text"
                    placeholder="Nhập địa điểm cần tìm..."
                    value={searchQuery}
                    onChange={(e) => {
                        setSearchQuery(e.target.value);
                        searchLocation();
                    }}
                    style={{ width: '70%', padding: '5px' }}
                />
                {suggestions.length > 0 && (
                    <ul
                        style={{
                            listStyleType: 'none',
                            padding: 0,
                            marginTop: 0,
                            backgroundColor: 'white',
                            border: '1px solid #ccc',
                            maxHeight: '150px',
                            overflowY: 'auto',
                            width: '70%',
                        }}
                    >
                        {suggestions.map((suggestion, index) => (
                            <li
                                key={index}
                                onClick={() => handleSuggestionClick(suggestion)}
                                style={{
                                    padding: '10px',
                                    cursor: 'pointer',
                                    borderBottom: '1px solid #eee',
                                }}
                            >
                                {suggestion.display_name}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
            <MapContainer
                center={[20.878722886100828, 105.72479266396617]}
                zoom={13}
                style={{ height: '400px', width: '100%' }}
            >
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                />
                {markerPosition && <Marker position={markerPosition} icon={customIcon} />}
                <MapComponent />
                <MapController position={markerPosition} />
            </MapContainer>
            {markerPosition && (
                <div>
                    <h4>Kinh độ và vĩ độ:</h4>
                    <p>Latitude: {markerPosition.lat}</p>
                    <p>Longitude: {markerPosition.lng}</p>
                </div>
            )}
            {address && (
                <div>
                    <h4>Địa chỉ:</h4>
                    <p>{address}</p>
                </div>
            )}
            {error && (
                <div style={{ color: 'red' }}>
                    <p>{error}</p>
                </div>
            )}
        </div>
    );
};

export default ReverseGeocode;
