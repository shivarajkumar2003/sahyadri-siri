# 🌿 SahyadriSiri

SahyadriSiri is a community-powered environmental reporting Android application designed to help users monitor and protect local water sources across villages, towns, and forest regions.

The app allows users to report:
- Water pollution
- River and stream conditions
- Waste dumping
- Water clarity issues
- Flow conditions
- Environmental hazards

Built using modern Android development technologies including Jetpack Compose, Firebase, Google Maps SDK, and MVVM architecture.

----

# 🚀 Features

## 📍 Real-Time Location Reporting
- Automatically fetches user's current GPS location
- Stores latitude and longitude with every report
- Converts coordinates into readable addresses

## 🗺 Interactive Google Maps
- View nearby reports on map
- Location-based reporting
- Marker-based navigation

## 📝 Pollution Reporting System
Users can submit:
- Stream / river name
- Water clarity condition
- Water flow speed
- Smell observations
- Pollution details
- Anonymous reports

## 🔥 Firebase Integration
- Firebase Authentication
- Firestore Database
- Realtime report storage
- Secure cloud backend

## 🎨 Modern UI
- Built fully with Jetpack Compose
- Material 3 design
- Smooth animations and responsive layouts

---

# 🛠 Tech Stack

| Technology | Usage |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | UI Framework |
| Firebase Auth | User Authentication |
| Firebase Firestore | Database |
| Google Maps SDK | Maps & Location |
| MVVM Architecture | App Structure |
| Coroutines | Async Operations |
| Material 3 | UI Components |

---

# 📂 Project Structure

```text
app/
├── data/
│   ├── model/
│   ├── repository/
│
├── ui/
│   ├── screens/
│   ├── components/
│   ├── theme/
│
├── viewmodel/
│
├── utils/
│
├── MainActivity.kt
```

---

# ⚙️ Setup Instructions

## 1️⃣ Clone Repository

```bash
git clone https://github.com/yourusername/sahyadrisiri.git
```

---

## 2️⃣ Open Project

Open project using:
- Android Studio Hedgehog or newer

---

## 3️⃣ Add Firebase

### Create Firebase Project

Visit:
https://console.firebase.google.com/

### Enable:
- Authentication
- Firestore Database

### Add Android App:
Use package name:

```text
com.sahyadrisiri.app
```

### Download:
```text
google-services.json
```

Place file inside:

```text
app/google-services.json
```

---

## 4️⃣ Add Google Maps API Key

Open:

```text
AndroidManifest.xml
```

Add:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_MAPS_API_KEY"/>
```

Enable:
- Maps SDK for Android
- Places API
- Geocoding API

Inside Google Cloud Console.

---

# 🔐 Required Permissions

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

---

# 📸 Screenshots

## Home Screen
- Interactive map
- Nearby reports
- Quick actions

## Report Form
- Pollution reporting interface
- Location-based submission

## User Profile
- Personal report history
- User information

---

# 🧠 Architecture

The app follows MVVM architecture:

```text
UI (Compose)
   ↓
ViewModel
   ↓
Repository
   ↓
Firebase / APIs
```

Benefits:
- Clean code
- Scalable structure
- Better state management
- Easier testing

---

# 🌍 Vision

SahyadriSiri aims to empower local communities to:
- Monitor water quality
- Report environmental issues
- Improve ecological awareness
- Build crowd-sourced environmental intelligence

---

# 🔮 Future Enhancements

- AI-based pollution analysis
- Offline report caching
- Push notifications
- Admin dashboard
- Heatmaps for pollution zones
- Photo/video upload support
- Water quality prediction system

---

# 👨‍💻 Developer

Developed by Yashas

Focused on building impactful technology solutions for environmental sustainability and community welfare.

---

# 📜 License

This project is licensed under the MIT License.

---

# ❤️ Acknowledgements

Special thanks to:
- Google Maps Platform
- Firebase
- Android Jetpack
- Open-source Android community
