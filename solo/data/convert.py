import json
import os

def combine_json_files(folder_path, output_file):
    combined_data = []
    for filename in os.listdir(folder_path):
        if filename.endswith(".json"):
            file_path = os.path.join(folder_path, filename)
            with open(file_path, 'r') as file:
                data = json.load(file)
                for year, songs in data.items():
                    for song in songs:
                        combined_data.append({
                            "year": int(year),
                            "name": song["name"],
                            "artists": song["artists"],
                            "duration": int(song["duration"])
                        })

    with open(output_file, 'w') as file:
        json.dump(combined_data, file, indent=4)

# usage
folder_path = './'
output_file = 'output.json'
combine_json_files(folder_path, output_file)
