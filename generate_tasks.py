#!/usr/bin/env python3

import uuid
import json
import sys
import os
import shutil
from pathlib import Path

def generate_tasks(count):
    # Get the parent directory of the current script location
    script_dir = Path(__file__).parent
    # Go up one level to get the parent of the project
    parent_dir = script_dir.parent
    # Create Tasks directory in the parent
    tasks_dir = parent_dir / "Tasks"

    # Clean the Tasks directory (remove all existing .json files)
    if tasks_dir.exists():
        print(f"Cleaning existing task files in {tasks_dir.absolute()}...")
        for json_file in tasks_dir.glob("*.json"):
            json_file.unlink()
            print(f"Removed {json_file.name}")

    # Create the Tasks directory if it doesn't exist
    tasks_dir.mkdir(exist_ok=True)

    print(f"Generating {count} task files in {tasks_dir.absolute()}...")

    for i in range(count):
        task_id = str(uuid.uuid4())
        task_data = {
            'Id': task_id,
            'description': f'Task {i+1}: Complete assigned work item'
        }

        # Create file in the Tasks directory
        filename = tasks_dir / f'{task_id}.json'
        with open(filename, 'w') as f:
            json.dump(task_data, f, indent=2)

        print(f'Created {filename.name}')

    print(f"Done! Generated {count} files in {tasks_dir.absolute()}")

if __name__ == "__main__":
    count = int(sys.argv[1]) if len(sys.argv) > 1 else 50
    generate_tasks(count)