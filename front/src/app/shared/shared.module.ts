import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { RouterModule } from '@angular/router';
import { BackButtonComponent } from './back-button/back-button.component';

@NgModule({
    declarations: [HeaderComponent, BackButtonComponent],
    imports: [
        CommonModule,
        MatIconModule,
        FlexLayoutModule,
        MatButtonModule,
        MatMenuModule,
        RouterModule
    ],
    exports: [HeaderComponent, BackButtonComponent]
})
export class SharedModule { }
