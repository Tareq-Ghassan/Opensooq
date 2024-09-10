
# OpenSooq Assignment

This project is a Kotlin-based Android application designed as part of an OpenSooq assignment. The application consists of three main screens: Categories, Subcategories, and Filters. The primary functionality of the app is to manage and display data from JSON files that represent API responses. This data is stored in a Realm database to ensure offline access and is dynamically updated if the JSON files have changed since the last launch.

## Features

- **Categories Screen**: Displays a list of categories retrieved from a JSON file. Users can search within the categories.
- **Subcategories Screen**: Displays subcategories based on the selected category. Users can also search within the subcategories.
- **Filter Screen**: Shows dynamic attributes for filtering, depending on the selected category and subcategory.

## Project Structure

The project follows the MVVM architectural pattern:

- **data**: Contains models and repository classes.
- **presentation**: Contains UI elements and ViewModel classes.

## Technical Details

- **Kotlin**: The primary language used for developing the app.
- **Realm Database**: Used for storing the JSON data locally on the device.
- **Coroutines**: Used for handling asynchronous tasks, such as checking if the JSON files have been updated and storing them in the Realm database.
- **Search Functionality**: Implemented in both the Categories and Subcategories screens.

## JSON Handling

Upon app launch, the app checks whether the JSON files (representing categories, subcategories, and filters) have already been stored in the Realm database. If the files are not stored or have changed, they are updated in the database. This process is managed using Kotlin coroutines to ensure smooth performance.

## How to Run the Project

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Tareq-Ghassan/opensooq-assignment.git
   ```
2. **Open the project in Android Studio**.
3. **Build and run the app** on an Android emulator or physical device.

## Future Enhancements

- Implementing additional filtering options in the Filter screen.
- Enhancing the UI/UX design.
- Adding unit tests to ensure the reliability of the data handling process.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

If you have any questions or feedback, feel free to reach out to me at

Linkedin: www.linkedin.com/in/Tareq-ghassan
