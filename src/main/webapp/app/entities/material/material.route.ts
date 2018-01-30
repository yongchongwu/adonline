import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MaterialComponent } from './material.component';
import { MaterialDetailComponent } from './material-detail.component';
import { MaterialPopupComponent } from './material-dialog.component';
import { MaterialDeletePopupComponent } from './material-delete-dialog.component';

@Injectable()
export class MaterialResolvePagingParams implements Resolve<any> {

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

export const materialRoute: Routes = [
    {
        path: 'material',
        component: MaterialComponent,
        resolve: {
            'pagingParams': MaterialResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.material.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'material/:id',
        component: MaterialDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.material.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialPopupRoute: Routes = [
    {
        path: 'material-new',
        component: MaterialPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.material.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'material/:id/edit',
        component: MaterialPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.material.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'material/:id/delete',
        component: MaterialDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adonlineApp.material.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
