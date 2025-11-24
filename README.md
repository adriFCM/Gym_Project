# Gym Class Booking – JavaFX App

JavaFX + SQLite desktop app for managing gym classes, bookings and member progress.

Layers:
- **domain** – core entities (User, GymClass, Booking, ClassSchedule, FitnessProgress)
- **repository** – persistence (SQLite via `sqlite-jdbc`)
- **service** – business logic
- **ui** – JavaFX controllers + FXML views
- **AppMain** – entry point

A default admin user is seeded at startup:
- **username:** `admin`
- **password:** `admin123`

---

## 1. Requirements

- **JDK 21** (or 17+, but project is configured for 21)
- **IntelliJ IDEA** (preferably Community Edition 2024+)
- **Internet access** (first run only, for Maven to download dependencies)
- **JavaFX SDK 21** (if you want to run via IntelliJ run configuration)
    - Download from OpenJFX and unzip, e.g. to  
      `C:\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9`

> SQLite is handled via Maven `org.xerial:sqlite-jdbc`, no manual JAR setup needed.

---

## 2. Getting the project

### Option A – Clone with Git (recommended)
```bash
git clone <repo-url>
cd Gym_Project
```

Then open the project in IntelliJ:

1. **File → Open…**
2. Select the `pom.xml` inside the project folder.
3. IntelliJ will recognise it as a Maven project – accept the import.

### Option B – Download ZIP

1. On GitHub: **Code → Download ZIP**.
2. Unzip somewhere (e.g. `C:\Users\...\Gym_Project`).
3. In IntelliJ: **File → Open…** and select the `pom.xml` in the unzipped folder.

The steps after this are the same.

---

## 3. IntelliJ configuration

### 3.1. Set project SDK

1. **File → Project Structure… → Project**
2. **Project SDK:** select **JDK 21**  
   (or point it to your installed JDK 21 folder).
3. **Apply / OK**.

### 3.2. Let Maven download dependencies

IntelliJ should do this automatically.

If not:
1. Open the **Maven** tool window (right side).
2. Click the **Reload/Refresh** icon.
3. Wait until the downloads finish (no red errors).

You should see dependencies like:
- `org.openjfx:javafx-controls:21.0.6`
- `org.xerial:sqlite-jdbc:3.43.0.0`
- etc.

---

## 4. JavaFX Run Configuration (IntelliJ)

Create a run configuration for the JavaFX app:

1. **Run → Edit Configurations…**
2. Click **+** → choose **Application** (or **JavaFX Application** if available).
3. Set:
    - **Name:** `AppMain`
    - **Main class:** `com.gym.AppMain`
    - **Use classpath of module:** `Gym_Project` (or whatever the module is called)
4. In **VM options** add (adapt the path to your JavaFX SDK):
```
--module-path "C:\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml
```

5. **Apply / OK**.

---

## 5. Running the app

1. **Run configuration:** select `AppMain`.
2. Click the green **Run** button.

On first launch you should see in the Run console:
- Initializing database…
- Table creation logs for `users`, `classes`, `class_schedule`, `bookings`, `fitness_progress`.
- Seeded default admin user: `admin` / `admin123` (only the first time).

The app window will open with the login screen.

Use the seeded credentials:
- **Username:** `admin`
- **Password:** `admin123`

Depending on the user role, the corresponding dashboard will be loaded.

The SQLite database file `gym_database.db` is created in the project directory automatically.

---

## 6. Notes for other machines (teacher / teammates)

As long as they:
- have **JDK 21**,
- open the project via `pom.xml`,
- let Maven import dependencies,
- and configure the JavaFX VM options path to their local JavaFX SDK,

the project should run without extra manual steps.

No one needs to copy JARs manually; everything is resolved via Maven.

---

## 7. Troubleshooting

**No suitable driver found for jdbc:sqlite:gym_database.db**  
→ `sqlite-jdbc` dependency wasn't downloaded.  
Open **Maven** tool window → **Reload project**.

**Location is not set or FXML load errors**  
→ The FXML path passed to `SceneManager.switchTo(...)` must match the file under `src/main/resources/views/`.