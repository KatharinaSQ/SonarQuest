import {Component, OnInit} from '@angular/core';
import {User} from '../../../Interfaces/User';
import {UserService} from '../../../services/user.service';
import {ImageService} from '../../../services/image.service';

@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html',
  styleUrls: ['./avatar.component.css']
})
export class AvatarComponent implements OnInit {

  public user: User;
  public img: any = '';
  public XPpercent = 0;

  constructor(
    private userService: UserService,
    private imageService: ImageService
  ) {
  }

  ngOnInit() {

    this.userService.user$.subscribe(user => {
      this.user = user;
      this.getAvatar();
      this.xpPercent();
    });
  }

  private getAvatar() {
    this.userService.getImage().subscribe((blob) => {
      this.imageService.createImageFromBlob(blob).subscribe(image => this.img = image);
    });
  }

  public xpPercent(): void {
    if (this.user.level != null && this.user.level.maxXp > 0 && this.user.xp > 0) {
      this.XPpercent = 100 / this.user.level.maxXp * this.user.xp;
    } else {
      this.XPpercent = 0;
    }
    this.XPpercent.toFixed(2);
  }
}
