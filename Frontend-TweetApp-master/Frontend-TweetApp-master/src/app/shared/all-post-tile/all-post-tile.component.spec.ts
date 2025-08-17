import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllPostTileComponent } from './all-post-tile.component';

describe('AllPostTileComponent', () => {
  let component: AllPostTileComponent;
  let fixture: ComponentFixture<AllPostTileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllPostTileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllPostTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
