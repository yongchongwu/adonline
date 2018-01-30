import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdonlineSharedModule } from '../../shared';
import {
    MaterialService,
    MaterialPopupService,
    MaterialComponent,
    MaterialDetailComponent,
    MaterialDialogComponent,
    MaterialPopupComponent,
    MaterialDeletePopupComponent,
    MaterialDeleteDialogComponent,
    materialRoute,
    materialPopupRoute,
    MaterialResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...materialRoute,
    ...materialPopupRoute,
];

@NgModule({
    imports: [
        AdonlineSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MaterialComponent,
        MaterialDetailComponent,
        MaterialDialogComponent,
        MaterialDeleteDialogComponent,
        MaterialPopupComponent,
        MaterialDeletePopupComponent,
    ],
    entryComponents: [
        MaterialComponent,
        MaterialDialogComponent,
        MaterialPopupComponent,
        MaterialDeleteDialogComponent,
        MaterialDeletePopupComponent,
    ],
    providers: [
        MaterialService,
        MaterialPopupService,
        MaterialResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdonlineMaterialModule {}
