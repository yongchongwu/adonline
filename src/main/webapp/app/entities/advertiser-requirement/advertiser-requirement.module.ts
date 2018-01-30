import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdonlineSharedModule } from '../../shared';
import {
    AdvertiserRequirementService,
    AdvertiserRequirementPopupService,
    AdvertiserRequirementComponent,
    AdvertiserRequirementDetailComponent,
    AdvertiserRequirementDialogComponent,
    AdvertiserRequirementPopupComponent,
    AdvertiserRequirementDeletePopupComponent,
    AdvertiserRequirementDeleteDialogComponent,
    advertiserRequirementRoute,
    advertiserRequirementPopupRoute,
    AdvertiserRequirementResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...advertiserRequirementRoute,
    ...advertiserRequirementPopupRoute,
];

@NgModule({
    imports: [
        AdonlineSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AdvertiserRequirementComponent,
        AdvertiserRequirementDetailComponent,
        AdvertiserRequirementDialogComponent,
        AdvertiserRequirementDeleteDialogComponent,
        AdvertiserRequirementPopupComponent,
        AdvertiserRequirementDeletePopupComponent,
    ],
    entryComponents: [
        AdvertiserRequirementComponent,
        AdvertiserRequirementDialogComponent,
        AdvertiserRequirementPopupComponent,
        AdvertiserRequirementDeleteDialogComponent,
        AdvertiserRequirementDeletePopupComponent,
    ],
    providers: [
        AdvertiserRequirementService,
        AdvertiserRequirementPopupService,
        AdvertiserRequirementResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdonlineAdvertiserRequirementModule {}
