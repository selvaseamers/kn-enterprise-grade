import { Component, Inject, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { City } from '../city';
import { ClientService } from '../client.service'


@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box.component.html',
  styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent {
  local_data: any;
  fileName = '';
  fileAttached: any;


  constructor(
    public dialogRef: MatDialogRef<DialogBoxComponent>,
    //@Optional() is used to prevent error if no data is passed
    @Optional() @Inject(MAT_DIALOG_DATA) public data: City, private clientService: ClientService) {
    this.local_data = { ...data };

  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0]
    this.fileName = file.name
    this.fileAttached = file as File

  }

  doAction() {
    this.clientService.updateCityDetails(this.local_data, this.fileAttached).subscribe(data => {
      this.local_data = data
      this.dialogRef.close({ data: this.local_data });
    });
  }

  closeDialog() {
    this.dialogRef.close({ event: 'Cancel' });
  }
}
