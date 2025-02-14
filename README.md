# Image Detection App

A fully functional **Android application** that performs **on-device image classification** using **TensorFlow Lite** and logs **detection time analytics** using **Firebase Analytics**.

## Screenshots

<p align="center">
  <img src="![WhatsApp Image 2025-02-14 at 3 31 42 PM (1)](https://github.com/user-attachments/assets/cfab519e-87a7-46b9-a7ad-db6d2aa01c1e)
" width="200"/>
  <img src="![WhatsApp Image 2025-02-14 at 3 31 42 PM (2)](https://github.com/user-attachments/assets/d02292f3-8a4c-4e6d-9358-1d153cd2cedc)
" width="200"/>
  <img src="![WhatsApp Image 2025-02-14 at 3 31 42 PM](https://github.com/user-attachments/assets/05f29f20-6e46-40ce-9f83-c776bf0d61dd)
" width="200"/>
  <img src="![WhatsApp Image 2025-02-14 at 3 31 41 PM](https://github.com/user-attachments/assets/478c1f25-6e93-42c1-8063-50c6ac544a07)
" width="200"/>
</p>

## Features

Detect objects in images using a **TensorFlow Lite** model  
Runs **entirely on-device** (no cloud processing required)  
Smooth **navigation with animations**  
Logs **detection time** with **Firebase Analytics**  
Stores detection history using **Room Database**  
Uses **Jetpack Compose** for UI  

---

## ML Model Details
- **Framework: TensorFlow Lite
- **Runs On-Device (no internet required)

## How It Works
- Loads an image from gallery/camera
- Passes it through TensorFlow Lite classifier
- Extracts the top predicted label
- Saves the detection details in Room Database
- Logs the detection time to Firebase Analytics

##APK Demo
- To generate a debug APK:

- Open Android Studio

- Click Build > Build Bundle(s) / APK(s) > Build APK(s)

## 📂 Project Setup

### 1️⃣ Prerequisites

- **Android Studio Flamingo (or later)**
- **Minimum SDK: 26 (Android 8.0)**
- **Internet connection** for Firebase setup (optional)

### 2️⃣ Clone the Project

```sh
git clone https://github.com/your-repo/ImageDetectionApp.git
cd ImageDetectionApp
