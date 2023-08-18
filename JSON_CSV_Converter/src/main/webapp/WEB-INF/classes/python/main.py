import json
import os
import shutil
import glob
import configparser
from pathlib import Path

ROOT = Path(__file__).parent

def fix_json_file(input_file):
    with open(input_file, 'r', encoding='utf-8') as file:
        json_string = file.read()
    
    json_string = json_string.strip()
    json_string = json_string.rstrip(',')
    json_objects = json_string.split('\n')
    json_string_fixed = ",".join(json_objects)
    json_string_fixed = f"[{json_string_fixed}]"

    return json_string_fixed

# Path to the config.properties file
config_file_path = ROOT.parent/"config.properties"
print(config_file_path)

# Initialize the configparser object
config = configparser.ConfigParser()

with open(config_file_path) as config_file:
    config.read_file(config_file)

# Read the configuration file
# config.read(config_file_path)

# Get the values of UPLOAD_DIRECTORY and OUTPUT_DIRECTORY
upload_directory = config.get("DEFAULT", "UPLOAD_DIRECTORY")
output_directory = config.get("DEFAULT", "OUTPUT_DIRECTORY")

file_extension = '*.json'
# directory_path = r'D:\Python\upload'
input_file = (glob.glob(os.path.join(upload_directory, file_extension))[0])

# input_file = r'D:\Python\upload\entities.json'
# output_directory = r'D:\Python\output'

if not os.path.exists(output_directory):
    os.mkdir(output_directory)
    print("Folder created successfully.")
else:
    for f in os.listdir(output_directory):
        os.remove(os.path.join(output_directory, f))
    print("The folder already exists.")

fixed_json = fix_json_file(input_file)
parsed_json_list = json.loads(fixed_json)

def save_data_to_file(data, schema, output_file):
    with open(output_file, 'a', encoding='utf-8') as file:
        file.write(json.dumps(data))
        file.write(',')

wanted_schemas = ["Address", "Airplane", "Associate", "BankAccount", "Company", "Directorship", "Employment", "Family", "Identification", "LegalEntity", "Membership", "Organization", "Ownership", "Passport", "Person", "Sanction", "Vessel"]

for data in parsed_json_list:
    schema = data.get("schema")
    if schema in wanted_schemas:
        output_file = os.path.join(output_directory, f"{schema}.json")
        save_data_to_file(data, schema, output_file)

print("Data classification and saving complete")

json_files = [file for file in os.listdir(output_directory) if file.endswith(".json")]

for json_file in json_files:
    file_path = os.path.join(output_directory, json_file)
    fixed_json = fix_json_file(file_path)
    # Create backup of the original file
    backup_path = file_path + ".bak"
    shutil.copy(file_path, backup_path)

    with open(file_path, 'w', encoding='utf-8') as file:
       file.write(fixed_json)
    
    os.remove(backup_path)

print("JSON files fixed and rewritten")

import os
import json
import pandas as pd

def json_to_csv(json_file, csv_file):
    json_file_path = os.path.join(output_directory, json_file)
    csv_file_path = os.path.join(output_directory, csv_file)

    with open(json_file_path, 'r') as json_data:
        data = json.load(json_data)

    df = pd.DataFrame(data)
    properties_data = df.pop('properties')

    properties_df = pd.DataFrame.from_dict(properties_data.to_dict()).T

    df = pd.concat([df, properties_df], axis=1)
    df.to_csv(csv_file_path, index=False)

json_files = [file for file in os.listdir(output_directory) if file.endswith('.json')]

for json_file in json_files:
    csv_file = json_file.replace('.json', '.csv')
    json_to_csv(json_file, csv_file)