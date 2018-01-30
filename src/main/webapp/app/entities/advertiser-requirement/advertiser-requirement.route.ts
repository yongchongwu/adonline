import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AdvertiserRequirementComponent } from './advertiser-requirement.component';
import { AdvertiserRequirementDetailComponent } from './advertiser-requirement-detail.component';
import { AdvertiserRequirementPopupComponent } from './advertiser-requirement-dialog.component';
import { AdvertiserRequirementDeletePopupComponent } from './advertiser-requirement-delete-dialog.component';

@Injectable()
export class AdvertiserRequirementResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const advertiserRequirementRoute: Routes = [
    {
        path: 'advertiser-requirement',
        component: AdvertiserRequirementComponent,
        resolve: {
            'pagingParams': AdvertiserRequirementResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.advertiserRequirement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'advertiser-requirement/:id',
        component: AdvertiserRequirementDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.advertiserRequirement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const advertiserRequirementPopupRoute: Routes = [
    {
        path: 'advertiser-requirement-new',
        component: AdvertiserRequirementPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.advertiserRequirement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'advertiser-requirement/:id/edit',
        component: AdvertiserRequirementPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.advertiserRequirement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'advertiser-requirement/:id/delete',
        component: AdvertiserRequirementDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.advertiserRequirement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
