# 🚨 IoT Smart Monitoring System 🚨

An IoT-based project for real-time monitoring of gas, flame, temperature, and humidity levels using ESP32 and an Android app.

---

## 🌟 Overview
This project integrates:
- 🛠️ **Sensors** for detecting gas levels, temperature, humidity, and flames.
- ⚙️ **Actuators** for controlling fans based on thresholds.
- 📱 **Android App** for real-time data visualization and alerts.
- ☁️ **Firebase** for Realtime Database and user authentication.

---

## ✨ Features
1. **Sensors**:
   - 💨 **MQ2 Gas Sensor**: Detects combustible gases and smoke.
   - 🌡️ **DHT11 Sensor**: Monitors temperature and humidity.
   - 🔥 **Flame Sensor**: Detects the presence of fire or flame.

2. **Actuators**:
   - 🌀 **Fan & Blue LED**: Automatically activated when temperature thresholds are exceeded.
   - 🔊 **Buzzer & Red LED**: Activated when smoke levels exceed the threshold.
   - 🟠 **Orange LED**: Lights up when a flame is detected.
   - 🟢 **Green LED**: Indicates safe conditions (no smoke).

3. **Android App**:
   - 📊 Real-time sensor data monitoring.
   - 📈 Data visualization with charts (Bar Charts, Line Charts) using the `MPAndroidChart` library.
   - 🚨 Safety alerts with visual indicators.

4. **Firebase Integration**:
   - 📂 **Realtime Database** for storing sensor data.

---

## 🛠️ Hardware Setup

### 🧰 Components Used
- 🤖 **ESP32 Microcontroller**
- 💨 **MQ2 Gas Sensor**
- 🌡️ **DHT11 Temperature & Humidity Sensor**
- 🔥 **Flame Sensor**
- 🌀 **Fan (Actuator)**
- 🔔 **Buzzer**
- 💡 **LEDs** (Green, Red, Orange, Blue)
- 🪛 **Resistors and Jumper Wires**
- 🧩 **Breadboard**
- ⚡ **5V Single-Channel Relay Module**

---

### ⚡ Pin Connections
| **Component**          | **ESP32 Pin**   | **Purpose**                                |
|-------------------------|-----------------|--------------------------------------------|
| 🌡️ **DHT11 Sensor**     | `15`            | Measures temperature and humidity          |
| 💨 **MQ2 Gas Sensor**   | `34`            | Detects gas levels (analog pin)            |
| 🔥 **Flame Sensor**     | `5`             | Detects flame (digital pin)                |
| 🟢 **Green LED**        | `16`            | Indicates no gas detection (safe)          |
| 🔴 **Red LED**          | `17`            | Indicates gas detection (alert)            |
| 🌀 **Fan (Actuator)**   | `18`            | Turns on/off based on temperature threshold|
| 🔔 **Buzzer**           | `19`            | Alerts for gas detection                   |
| 🟠 **Orange LED**       | `4`             | Indicates flame detection                  |
| 🔵 **Blue LED**         | `22`            | Indicates system OK or fan status          |

---

## 💻 Software Setup

### 🔧 Arduino IDE
1. Install the [Arduino IDE](https://www.arduino.cc/en/software).
2. Add the **ESP32** board support package.
3. Install the required libraries:
   - 🛠️ `Adafruit_Sensor`
   - 🌡️ `DHT`
   - ☁️ `Firebase ESP32`
4. Upload the code from the `ESP32_Code` folder to your ESP32 board.

### 📱 Android App
1. Open the `Android_App` folder in **Android Studio**.
2. Connect the app to the Firebase project named **liviru**.
   - Add the `google-services.json` file to the `app/` folder.
3. Build and run the app on your device.

---

## ☁️ Firebase Configuration
1. Go to the [Firebase Console](https://console.firebase.google.com) and open the **liviru** project.
2. Set up:
   - 📂 **Realtime Database** for storing sensor data.

---

## 🚀 How to Run the Project
1. **Hardware Setup**:
   - Assemble the circuit based on the connections above.
   - Power the ESP32.

2. **ESP32 Code**:
   - Upload the code using Arduino IDE.

3. **Android App**:
   - Install the app on your Android device.

4. **Monitor Data**:
   - View real-time data in the app.
   - Observe safety alerts for abnormal conditions.

---

## ❓ Frequently Asked Questions (FAQs)

### ❓ Why use a relay for the fan?
A relay is used to control the fan because the ESP32 cannot directly handle the higher current or voltage required by the fan. The relay acts as a switch that safely connects the fan to an external power source.

### ❓ Why use an external Arduino board to power the MQ2 sensor?
The MQ2 sensor requires a stable 5V power supply for accurate readings. While the ESP32 can provide 3.3V, this is insufficient for the MQ2 sensor's operation.

### ❓ Why use 220Ω resistors for LEDs?
Resistors are used to limit the current flowing through the LEDs, protecting them from burning out. The 220Ω resistor ensures the LED operates within safe current limits.

---

## 🌟 Future Enhancements
- 🔔 Implement push notifications for safety alerts.
- 🛠️ Allow users to set custom thresholds for gas, temperature, and flame detection.
- 📊 Add historical data visualization for trends and analysis.

---

## 🔗 Useful Resources
- [ESP32 Documentation](https://docs.espressif.com/projects/esp-idf/en/latest/esp32/)
- [Firebase Realtime Database](https://firebase.google.com/products/realtime-database)
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
