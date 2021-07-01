import {Component, OnInit} from '@angular/core';
import {User} from '../../Interfaces/User';
import {UserService} from '../../services/user.service';
import {ImageService} from '../../services/image.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public user: User;
  public img: any = '';

  constructor(private userService: UserService, private imageService: ImageService) {
    this.userService.user$.subscribe(user => {
      this.user = user;
      this.getAvatar();
    });

  }

  private getAvatar() {
    if (this.user) {
      this.userService.getImage().subscribe((blob) => {
        this.imageService.createImageFromBlob(blob).subscribe(image => this.img = image);
      });
    }
  }


  ngOnInit() {
  }

}
