import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowArtefactComponent} from './show-artefact.component';

describe('ShowArtefactComponent', () => {
  let component: ShowArtefactComponent;
  let fixture: ComponentFixture<ShowArtefactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShowArtefactComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowArtefactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
