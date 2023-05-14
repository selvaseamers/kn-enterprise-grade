import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { CityListPaginated } from '../city-list';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { City } from '../city';
import {ClientService} from '../client.service'
import { DialogBoxComponent } from '../dialog-box/dialog-box.component';
import { MatDialog } from '@angular/material/dialog';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements AfterViewInit {

  displayedColumns: any[]
  public cityListPaginated: any;
  dataSource: MatTableDataSource<any[]> = new MatTableDataSource<any[]>([]);
  canEdit = localStorage.getItem('ROLE')?.includes('ROLE_ALLOW_EDIT')

  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(private authService: AuthService, private router: Router, private http: HttpClient, private clientService: ClientService, public dialog: MatDialog) {
    this.getCityList();
    this.dataSource.filterPredicate = function (dataSource, filter) {
      return true;
    }
    if (this.canEdit) { this.displayedColumns = ['no', 'name', 'image', 'action'] }
    else { this.displayedColumns = ['no', 'name', 'image'] }
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  editData(cityList: City) {
    const dialogRef = this.dialog.open(DialogBoxComponent, {
      height:'350px',width:'350px',
      data:cityList
    });
    dialogRef.afterClosed().subscribe(result => {
      this.updateRowData(result.data)
  }); 
  }

  updateRowData(row_obj:City){
    this.dataSource.data = this.dataSource.data.filter((element: any) => {
      if(element.id == row_obj.id){
        element.cityName = row_obj.cityName;
        element.imageUrl = row_obj.imageUrl;
      }
      return true;
    })
  }


  logout() {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

  getCityList() {
    this.clientService.getCityList().subscribe(res => {
      this.cityListPaginated = JSON.parse(JSON.stringify(res))
      this.dataSource = new MatTableDataSource(this.cityListPaginated.cityList);
      this.dataSource.paginator = this.paginator;
    }, (error) => {
      console.error('error caught in component');
      console.log(error);
      if (error.status == 401) {
        console.log('redirecting to login');
        this.router.navigateByUrl('/login');
      }
    })
  }
}

